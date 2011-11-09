package com.dayatang.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * ConfigurationFileImpl为读取/回写配置文件的工具类，一个实例大概对应了一个物理配置文件，可以使用
 * getXxx("aa.conf")，getXxx("/a/b","xx.conf") 获得/a/b/xx.conf配置文件。 具体配置大致采用
 * ConfigurationFileImpl.getXxx(key)的方式读取。
 * 每个配置项用key --> value 的方式组织，推荐采用点分字符串的方式编制key部分，如果不指定前缀，
 * usePrefix()激活配置项前缀功能，你可以通过usePrefix("xxx.xxx")设置某个具体实例的前缀。
 * 前缀的作用在于减少复杂性，如果我们在配置文件里有cn.com.pzhsteel.smbserverhost=smbserverhost.com
 * 这一项，并且不更改默认前缀的话，getXxx("smbserverhost")和get("com.dayatang.smbserverhost")
 * 将会返回同样的结果。
 * 如果要使用别人已指定前缀的配置文件，又想使用这个功能的话，请首先了解前缀是什么，然后调用usePrefix设置前缀，使用这一方便的功能。
 * 配置文件的格式符合标准的java属性文件格式，采用UTF8的编码方式，支持中文，不需native2ascii。
 * 
 * @author yyang
 */
public class ConfigurationFileImpl implements WritableConfiguration {
	private PropertiesFileUtils pfu = new PropertiesFileUtils("utf-8");
	private URL fileUrl;
	private String prefix = "";
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Hashtable<String, String> hTable = null;
	
	/**
	 * 从类路径读入配置文件
	 * @param fileName
	 * @return
	 */
	public static ConfigurationFileImpl fromClasspath(final String fileName) {
		URL url = ConfigurationFileImpl.class.getResource(fileName);
		return new ConfigurationFileImpl(url);
	}
	
	/**
	 * 从文件系统读入配置文件
	 * @param pathname
	 * @return
	 */
	public static ConfigurationFileImpl fromFileSystem(final String pathname) {
		return fromFileSystem(new File(pathname));
	}
	
	/**
	 * 从文件系统读入配置文件
	 * @param dirPath
	 * @param fileName
	 * @return
	 */
	public static ConfigurationFileImpl fromFileSystem(final String dirPath, final String fileName) {
		if (StringUtils.isEmpty(dirPath)) {
			return fromFileSystem(fileName);
		}
		return fromFileSystem(new File(dirPath, fileName));
	}
	
	private static ConfigurationFileImpl fromFileSystem(final File file) {
		if (!file.exists()) {
			throw new RuntimeException("File " + file.getName() + " not exists!");
		}
		if (!file.canRead()) {
			throw new RuntimeException("File " + file.getName() + " is unreadable!");
		}
		try {
			return new ConfigurationFileImpl(file.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private ConfigurationFileImpl(final URL url) {
		if (url == null) {
			throw new RuntimeException("url is null!");
		}
		this.fileUrl = url;
		loadFile();
	}

	/**
	 * 激活配置前缀功能
	 * 
	 * @param prefix 如"com.dayatang.mes."
	 */
	public void usePrefix(final String prefix) {
		if (StringUtils.isNotEmpty(prefix)) {
			this.prefix = prefix.endsWith(".") ? prefix : prefix + ".";
		}
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setDatePattern(java.lang.String)
	 */
	@Override
	public void setDatePattern(String datePattern) {
		dateFormat = new SimpleDateFormat(datePattern);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getString(java.lang.String, java.lang.String)
	 */
	@Override
	public String getString(String key, String defaultValue) {
		if (StringUtils.isEmpty(key)) {
			throw new IllegalArgumentException("Key is null or empty!");
		}
		String result = hTable.get(key);
		if (result != null) {
			return result;
		}
		result = hTable.get(prefix + key);
		return result == null ? defaultValue : result;
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getString(java.lang.String)
	 */
	@Override
	public String getString(String key) {
		return getString(key, "");
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setString(java.lang.String, java.lang.String)
	 */
	@Override
	public void setString(String key, String value) {
		if (StringUtils.isEmpty(key)) {
			throw new IllegalArgumentException("Key is null or empty!");
		}
		hTable.put(key, value == null ? "" : StringPropertyReplacer.replaceProperties(value));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getInt(java.lang.String, int)
	 */
	@Override
	public int getInt(String key, int defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return Integer.parseInt(result);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String key) {
		return getInt(key, 0);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setInt(java.lang.String, int)
	 */
	@Override
	public void setInt(String key, int value) {
		setString(key, String.valueOf(value));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getLong(java.lang.String, long)
	 */
	@Override
	public long getLong(String key, long defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return Long.parseLong(result);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getLong(java.lang.String)
	 */
	@Override
	public long getLong(String key) {
		return getLong(key, 0);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setLong(java.lang.String, long)
	 */
	@Override
	public void setLong(String key, long value) {
		setString(key, String.valueOf(value));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getDouble(java.lang.String, double)
	 */
	@Override
	public double getDouble(String key, double defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return Double.parseDouble(result);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(String key) {
		return getDouble(key, 0);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setDouble(java.lang.String, double)
	 */
	@Override
	public void setDouble(String key, double value) {
		setString(key, String.valueOf(value));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getBoolean(java.lang.String, boolean)
	 */
	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return Boolean.parseBoolean(result);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setBoolean(java.lang.String, boolean)
	 */
	@Override
	public void setBoolean(String key, boolean value) {
		setString(key, String.valueOf(value));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getDate(java.lang.String, java.util.Date)
	 */
	@Override
	public Date getDate(String key, Date defaultValue) {
		String result = getString(key);
		if (StringUtils.isEmpty(result)) {
			return defaultValue;
		}
		try {
			return dateFormat.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(String key) {
		return getDate(key, null);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setDate(java.lang.String, java.util.Date)
	 */
	@Override
	public void setDate(String key, Date value) {
		if (value == null) {
			setString(key, "");
		}
		setString(key, dateFormat.format(value));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#save()
	 */
	@Override
	public void save() {
		try {
			File file = new File(fileUrl.getFile());
			Properties props = pfu.unRectifyProperties(hTable);
			store(props, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "8859_1")), "Config file for " + fileUrl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void store(Properties props, BufferedWriter bw, String comments) throws IOException {
		if (!StringUtils.isEmpty(comments)) {
			bw.append("#" + comments);
			bw.newLine();
		}
		bw.write("#" + new Date().toString());
		bw.newLine();
		synchronized (this) {
			for (Object propKey : props.keySet()) {
				String key = convertString(propKey.toString(), true);
				String value = convertString((String) props.get(propKey), false);
				bw.write(key + "=" + value);
				bw.newLine();
			}
			bw.flush();
		}
		bw.close();
	}

	private String convertString(String theString, boolean escapeSpace) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuilder outBuffer = new StringBuilder(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			// Handle common case first, selecting largest block that
			// avoids the specials below
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			switch (aChar) {
			case ' ':
				if (x == 0 || escapeSpace)
					outBuffer.append('\\');
				outBuffer.append(' ');
				break;
			case '\t':
				outBuffer.append('\\');
				outBuffer.append('t');
				break;
			case '\n':
				outBuffer.append('\\');
				outBuffer.append('n');
				break;
			case '\r':
				outBuffer.append('\\');
				outBuffer.append('r');
				break;
			case '\f':
				outBuffer.append('\\');
				outBuffer.append('f');
				break;
			case '=': // Fall through
			case ':': // Fall through
			case '#': // Fall through
			case '!':
				outBuffer.append('\\');
				outBuffer.append(aChar);
				break;
			default:
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

	URL getFileUrl() {
		return fileUrl;
	}

	public Properties getProperties() {
		Properties results = new Properties();
		results.putAll(this.hTable);
		return results;
	}

	public String toString() {
		return getClass().getSimpleName() + "{" + fileUrl + "}";
	}

	private void loadFile() {
		hTable = new Hashtable<String, String>();
		Properties props = new Properties();
		try {
			if (this.fileUrl != null) {
				props.load(this.fileUrl.openStream());
				hTable = pfu.rectifyProperties(props);
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot load config file: " + fileUrl, e);
		}
	}
}