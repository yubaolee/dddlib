package com.dayatang.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * <P>ConfigurationUrlImpl为读取远程配置文件的工具类，一个实例大概对应了一个远程配置文件，具体配置大致采用
 * ConfigurationUrlImpl.getXxx(key)的方式读取。</P>
 * <P>每个配置项用key --> value 的方式组织，推荐采用点分字符串的方式编制key部分。 usePrefix()激活
 * 配置项前缀功能，你可以通过usePrefix("xxx.xxx")设置某个具体实例的前缀。</P>
 * <P>前缀的作用在于减少复杂性，如果我们在配置文件里有com.dayatang.smbserverhost=smbserverhost.com
 * 这一项，并且不更改默认前缀的话，getXxx("smbserverhost")和get("com.dayatang.smbserverhost")
 * 将会返回同样的结果。</P>
 * <P>配置文件的格式符合标准的java属性文件格式，采用UTF8的编码方式，支持中文，不需native2ascii。</P>
 * <P>注意：为了避免日期格式的转换等复杂问题，日期是转化为long类型的数据保存的（采用date.getTime()方法）。</P>
 * 
 * @author yyang
 */
public class ConfigurationUrlImpl extends AbstractConfiguration {
	private static final Slf4jLogger LOGGER = Slf4jLogger.of(ConfigurationUrlImpl.class);
	private PropertiesFileUtils pfu = new PropertiesFileUtils("utf-8");
	private URL url;
	private Hashtable<String, String> hTable;	
	
	public static ConfigurationUrlImpl fromUrl(final URL url) {
		return new ConfigurationUrlImpl(url);
	}

	private ConfigurationUrlImpl(final URL url) {
		this.url = url;
		load();
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#save()
	 */
	@Override
	public void save() {
		try {
			File file = new File(url.getFile());
			if (!file.exists() || !file.canWrite()) {
				throw new UnsupportedOperationException("Persistence is not supported!");
			}
			Properties props = pfu.unRectifyProperties(getHashtable());
			store(props, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), PropertiesFileUtils.ISO_8859_1)), "Config file for " + url);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void store(Properties props, BufferedWriter out, String comments) throws IOException {
		if (StringUtils.isNotEmpty(comments)) {
			out.append("#" + comments);
			out.newLine();
		}
		out.write("#" + new Date().toString());
		out.newLine();
		synchronized (this) {
			for (Object propKey : props.keySet()) {
				String key = convertString(propKey.toString(), true);
				String value = convertString((String) props.get(propKey), false);
				out.write(key + "=" + value);
				out.newLine();
			}
			out.flush();
		}
		out.close();
		LOGGER.debug("Save configuration to {} at {}", url.getFile(), new Date());
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

	URL getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" + url + "}";
	}

	@Override
	public Properties getProperties() {
		return pfu.unRectifyProperties(getHashtable());
	}

	@Override
	public void load() {
		hTable = new Hashtable<String, String>();
		Properties props = new Properties();
		try {
			if (url != null) {
				props.load(url.openStream());
				hTable = pfu.rectifyProperties(props);
				LOGGER.debug("Load configuration from {} at {}", url.getFile(), new Date());
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot load config file: " + url, e);
		}
	}

	@Override
	public Hashtable<String, String> getHashtable() {
		if (hTable == null) {
			load();
		}
		return hTable;
	}
}