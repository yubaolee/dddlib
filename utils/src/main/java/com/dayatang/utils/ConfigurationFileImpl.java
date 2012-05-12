package com.dayatang.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * <P>ConfigurationFileImpl为读取/回写配置文件的工具类，一个实例大概对应了一个物理配置文件，可以使用
 * getXxx("aa.conf")，getXxx("/a/b","xx.conf") 获得/a/b/xx.conf配置文件。 具体配置大致采用
 * ConfigurationFileImpl.getXxx(key)的方式读取。</P>
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
public class ConfigurationFileImpl extends AbstractConfiguration {
	private PropertiesFileUtils pfu = new PropertiesFileUtils("utf-8");
	private URL fileUrl;
	private Hashtable<String, String> hTable;	
	/**
	 * 从类路径读入配置文件
	 * @param fileName
	 * @return
	 */
	public static ConfigurationFileImpl fromClasspath(final String fileName) {
		URL url = ConfigurationFileImpl.class.getResource(fileName);
		if (url == null) {
			throw new RuntimeException("File " + fileName + " not found!");
		}
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
			throw new RuntimeException("File " + file.getName() + " not found!");
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
		this.fileUrl = url;
		refresh();
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#save()
	 */
	@Override
	public void save() {
		try {
			File file = new File(fileUrl.getFile());
			Properties props = pfu.unRectifyProperties(getHashtable());
			store(props, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), PropertiesFileUtils.ISO_8859_1)), "Config file for " + fileUrl);
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" + fileUrl + "}";
	}

	@Override
	public void refresh() {
		hTable = new Hashtable<String, String>();
		Properties props = new Properties();
		try {
			if (fileUrl != null) {
				props.load(fileUrl.openStream());
				hTable = pfu.rectifyProperties(props);
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot load config file: " + fileUrl, e);
		}
	}

	@Override
	public Hashtable<String, String> getHashtable() {
		if (hTable == null) {
			refresh();
		}
		return hTable;
	}
}