package com.dayatang.i18n.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.i18n.I18nService;
import com.dayatang.i18n.NoSuchMessageException;

public class ResourceBundleI18nService implements I18nService {

	private Logger logger = LoggerFactory.getLogger(ResourceBundleI18nService.class); 
	
	private String[] basenames = new String[0];

	/**
	 * Cache to hold loaded ResourceBundles.
	 * This Map is keyed with the bundle basename, which holds a Map that is
	 * keyed with the Locale and in turn holds the ResourceBundle instances.
	 * This allows for very efficient hash lookups, significantly faster
	 * than the ResourceBundle class's own cache.
	 */
	private final Map<String, Map<Locale, ResourceBundle>> cachedResourceBundles =
			new HashMap<String, Map<Locale, ResourceBundle>>();

	/**
	 * Cache to hold already generated MessageFormats.
	 * This Map is keyed with the ResourceBundle, which holds a Map that is
	 * keyed with the message code, which in turn holds a Map that is keyed
	 * with the Locale and holds the MessageFormat values. This allows for
	 * very efficient hash lookups without concatenated keys.
	 * @see #getMessageFormat
	 */
	private final Map<ResourceBundle, Map<String, Map<Locale, MessageFormat>>> cachedBundleMessageFormats =
			new HashMap<ResourceBundle, Map<String, Map<Locale, MessageFormat>>>();



	/**
	 * Set a single basename, following {@link java.util.ResourceBundle} conventions:
	 * essentially, a fully-qualified classpath location. If it doesn't contain a
	 * package qualifier (such as <code>org.mypackage</code>), it will be resolved
	 * from the classpath root.
	 * <p>Messages will normally be held in the "/lib" or "/classes" directory of
	 * a web application's WAR structure. They can also be held in jar files on
	 * the class path.
	 * <p>Note that ResourceBundle names are effectively classpath locations: As a
	 * consequence, the JDK's standard ResourceBundle treats dots as package separators.
	 * This means that "test.theme" is effectively equivalent to "test/theme",
	 * just like it is for programmatic <code>java.util.ResourceBundle</code> usage.
	 * @see #setBasenames
	 * @see java.util.ResourceBundle#getBundle(String)
	 */
	public void setBasename(String basename) {
		setBasenames(new String[] {basename});
	}

	/**
	 * Set an array of basenames, each following {@link java.util.ResourceBundle}
	 * conventions: essentially, a fully-qualified classpath location. If it
	 * doesn't contain a package qualifier (such as <code>org.mypackage</code>),
	 * it will be resolved from the classpath root.
	 * <p>The associated resource bundles will be checked sequentially
	 * when resolving a message code. Note that message definitions in a
	 * <i>previous</i> resource bundle will override ones in a later bundle,
	 * due to the sequential lookup.
	 * <p>Note that ResourceBundle names are effectively classpath locations: As a
	 * consequence, the JDK's standard ResourceBundle treats dots as package separators.
	 * This means that "test.theme" is effectively equivalent to "test/theme",
	 * just like it is for programmatic <code>java.util.ResourceBundle</code> usage.
	 * @see #setBasename
	 * @see java.util.ResourceBundle#getBundle(String)
	 */
	public void setBasenames(String[] basenames)  {
		if (basenames != null) {
			this.basenames = new String[basenames.length];
			for (int i = 0; i < basenames.length; i++) {
				String basename = basenames[i];
				this.basenames[i] = basename.trim();
			}
		}
		else {
			this.basenames = new String[0];
		}
	}


	/**
	 * Resolves the given message code as key in the registered resource bundles,
	 * returning the value found in the bundle as-is (without MessageFormat parsing).
	 */
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		String result = null;
		for (int i = 0; result == null && i < this.basenames.length; i++) {
			ResourceBundle bundle = getResourceBundle(this.basenames[i], locale);
			if (bundle != null) {
				result = getStringOrNull(bundle, code);
			}
		}
		return result;
	}

	/**
	 * Resolves the given message code as key in the registered resource bundles,
	 * using a cached MessageFormat instance per message code.
	 */
	protected MessageFormat resolveCode(String code, Locale locale) {
		MessageFormat messageFormat = null;
		for (int i = 0; messageFormat == null && i < this.basenames.length; i++) {
			ResourceBundle bundle = getResourceBundle(this.basenames[i], locale);
			if (bundle != null) {
				messageFormat = getMessageFormat(bundle, code, locale);
			}
		}
		return messageFormat;
	}

	private String getStringOrNull(ResourceBundle bundle, String key) {
		try {
			return bundle.getString(key);
		}
		catch (MissingResourceException ex) {
			// Assume key not found
			// -> do NOT throw the exception to allow for checking parent message source.
			return null;
		}
	}

	/**
	 * Return a MessageFormat for the given bundle and code,
	 * fetching already generated MessageFormats from the cache.
	 * @param bundle the ResourceBundle to work on
	 * @param code the message code to retrieve
	 * @param locale the Locale to use to build the MessageFormat
	 * @return the resulting MessageFormat, or <code>null</code> if no message
	 * defined for the given code
	 * @throws MissingResourceException if thrown by the ResourceBundle
	 */
	private MessageFormat getMessageFormat(ResourceBundle bundle, String code, Locale locale)
			throws MissingResourceException {

		synchronized (this.cachedBundleMessageFormats) {
			Map<String, Map<Locale, MessageFormat>> codeMap = this.cachedBundleMessageFormats.get(bundle);
			Map<Locale, MessageFormat> localeMap = null;
			if (codeMap != null) {
				localeMap = codeMap.get(code);
				if (localeMap != null) {
					MessageFormat result = localeMap.get(locale);
					if (result != null) {
						return result;
					}
				}
			}

			String msg = getStringOrNull(bundle, code);
			if (msg != null) {
				if (codeMap == null) {
					codeMap = new HashMap<String, Map<Locale, MessageFormat>>();
					this.cachedBundleMessageFormats.put(bundle, codeMap);
				}
				if (localeMap == null) {
					localeMap = new HashMap<Locale, MessageFormat>();
					codeMap.put(code, localeMap);
				}
				MessageFormat result = createMessageFormat(msg, locale);
				localeMap.put(locale, result);
				return result;
			}

			return null;
		}
	}

	/**
	 * Create a MessageFormat for the given message and Locale.
	 * @param msg the message to create a MessageFormat for
	 * @param locale the Locale to create a MessageFormat for
	 * @return the MessageFormat instance
	 */
	protected MessageFormat createMessageFormat(String msg, Locale locale) {
		return new MessageFormat((msg != null ? msg : ""), locale);
	}


	/**
	 * Return a ResourceBundle for the given basename and code,
	 * fetching already generated MessageFormats from the cache.
	 * @param basename the basename of the ResourceBundle
	 * @param locale the Locale to find the ResourceBundle for
	 * @return the resulting ResourceBundle, or <code>null</code> if none
	 * found for the given basename and Locale
	 */
	protected ResourceBundle getResourceBundle(String basename, Locale locale) {
		synchronized (this.cachedResourceBundles) {
			Map<Locale, ResourceBundle> localeMap = this.cachedResourceBundles.get(basename);
			if (localeMap != null) {
				ResourceBundle bundle = localeMap.get(locale);
				if (bundle != null) {
					return bundle;
				}
			}
			try {
				ResourceBundle bundle = doGetBundle(basename, locale);
				if (localeMap == null) {
					localeMap = new HashMap<Locale, ResourceBundle>();
					this.cachedResourceBundles.put(basename, localeMap);
				}
				localeMap.put(locale, bundle);
				return bundle;
			}
			catch (MissingResourceException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("ResourceBundle [" + basename + "] not found for MessageSource: " + ex.getMessage());
				}
				// Assume bundle not found
				// -> do NOT throw the exception to allow for checking parent message source.
				return null;
			}
		}
	}

	/**
	 * Obtain the resource bundle for the given basename and Locale.
	 * @param basename the basename to look for
	 * @param locale the Locale to look for
	 * @return the corresponding ResourceBundle
	 * @throws MissingResourceException if no matching bundle could be found
	 * @see java.util.ResourceBundle#getBundle(String, java.util.Locale, ClassLoader)
	 * @see #getBundleClassLoader()
	 */
	protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
		return ResourceBundle.getBundle(basename, locale, getClass().getClassLoader());
	}

	@Override
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		// TODO Auto-generated method stub
		return null;
	}

}
