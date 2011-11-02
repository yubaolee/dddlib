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
 * ConfigUtils为读取/回写配置文件的工具类，一个实例大概对应了一个物理配置文件，可以使用
 * ConfigUtils.getConfigSet("aa.conf")，ConfigUtils.getConfigSet("/a/b",
 * "xx.conf") 获得/a/b/xx.conf配置文件。 具体配置大致采用 configUtils.getConfig(key)的方式读取。
 * 每个配置项用key --> value 的方式组织，推荐采用点分字符串的方式编制key部分，如果不指定前缀，
 * configUtils.usePrefix()激活配置项前缀功能，缺省采用"com.dayatang."
 * 作为统一前缀，你可以通过configUtils.usePrefix("xxx.xxx")设置某个具体实例的前缀，
 * 前缀的作用在于减少复杂性，如果我们在配置文件里有cn
 * .com.pzhsteel.smbserverhost=smbserverhost.com这一项，我们可以
 * 并且不更改默认前缀的话，configUtils.
 * getConfig("smbserverhost")和configUtils.get("com.dayatang.smbserverhost")
 * 将会返回同样的结果。
 * 如果要使用别人已指定前缀的配置文件，又想使用这个功能的话，请首先了解前缀是什么，然后调用usePrefix设置前缀，使用这一方便的功能。
 * 配置文件的格式符合标准的java属性文件格式，采用UTF8的编码方式，支持中文，不需native2ascii。
 * 
 * @author jzhai
 */
public class ConfigUtils {

	public static final String DEFAULT_PREFIX = "com.dayatang.";
	private PropertiesFileUtils pfu = new PropertiesFileUtils("utf-8");

	private URL fileUrl;
	private String prefix = "";
	private boolean usePrefix = false;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Hashtable<String, String> hTable = null;
	
	/**
	 * 从类路径读入配置文件
	 * @param fileName
	 * @return
	 */
	public static ConfigUtils fromClasspath(final String fileName) {
		URL url = ConfigUtils.class.getResource(fileName);
		return new ConfigUtils(url);
	}
	
	/**
	 * 从文件系统读入配置文件
	 * @param pathname
	 * @return
	 */
	public static ConfigUtils fromFileSystem(final String pathname) {
		File file = new File(pathname);
		if (!file.exists()) {
			throw new RuntimeException("File " + pathname + " not exists!");
		}
		if (!file.canRead()) {
			throw new RuntimeException("File " + pathname + " is unreadable!");
		}
		try {
			return new ConfigUtils(file.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 从文件系统读入配置文件
	 * @param dirPath
	 * @param fileName
	 * @return
	 */
	public static ConfigUtils fromFileSystem(final String dirPath, final String fileName) {
		if (StringUtils.isEmpty(dirPath)) {
			return fromFileSystem(fileName);
		}
		File dir = new File(dirPath);
		if (!dir.exists()) {
			throw new RuntimeException("Directory " + dir + " not exists!");
		}
		File file = new File(dirPath, fileName);
		if (!file.exists()) {
			throw new RuntimeException("File " + file + " not exists!");
		}
		if (!file.canRead()) {
			throw new RuntimeException("File " + file + " is unreadable!");
		}
		try {
			return new ConfigUtils(file.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private ConfigUtils(URL url) {
		if (url == null) {
			throw new RuntimeException("url is null!");
		}
		this.fileUrl = url;
		loadFile();
	}

	/**
	 * 激活配置前缀功能，默认为"com.dayatang."前缀
	 */
	public void usePrefix() {
		usePrefix(DEFAULT_PREFIX);
	}

	/**
	 * 激活配置前缀功能
	 * 
	 * @param prefix 如"com.dayatang.mes."
	 */
	public void usePrefix(final String prefix) {
		if (StringUtils.isEmpty(prefix)) {
			this.prefix = "";
			usePrefix = false;
			return;
		}
		usePrefix = true;
		this.prefix = prefix.endsWith(".") ? prefix : prefix + ".";
	}

	public void setDatePattern(String datePattern) {
		dateFormat = new SimpleDateFormat(datePattern);
	}

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @param defaultValue
	 *            配置文件中找不到则返回默认值
	 * @return 返回的配置值
	 */
	public String getString(String key, String defaultValue) {
		if (StringUtils.isEmpty(key)) {
			throw new IllegalArgumentException("Key is null or empty!");
		}
		String result = hTable.get(key);
		if (result != null) {
			return result;
		}
		if (usePrefix) {
			result = hTable.get(prefix + key);
		}
		return result == null ? defaultValue : result;
	}

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @return 返回的配置值
	 */
	public String getString(String key) {
		return getString(key, "");
	}

	/**
	 * 设置配置值
	 * 
	 * @param key
	 *            配置项关键字
	 * @param value
	 *            值
	 */
	public void setString(String key, String value) {
		if (StringUtils.isEmpty(key)) {
			throw new IllegalArgumentException("Key is null or empty!");
		}
		hTable.put(key, value == null ? "" : StringPropertyReplacer.replaceProperties(value));
	}

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @param defaultValue
	 *            配置文件中找不到则返回默认值
	 * @return 返回的配置值
	 */
	public int getInt(String key, int defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return Integer.parseInt(result);
	}

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @return 返回的配置值 找不到返回0
	 */
	public int getInt(String key) {
		return getInt(key, 0);
	}

	/**
	 * 设置配置值
	 * 
	 * @param key
	 *            配置项关键字
	 * @param value
	 *            值
	 */
	public void setInt(String key, int value) {
		setString(key, String.valueOf(value));
	}

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @param defaultValue
	 *            配置文件中找不到则返回默认值
	 * @return 返回的配置值
	 */
	public long getLong(String key, long defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return Long.parseLong(result);
	}

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @return 返回的配置值 找不到返回0
	 */
	public long getLong(String key) {
		return getLong(key, 0);
	}

	/**
	 * 设置配置值
	 * 
	 * @param key
	 *            配置项关键字
	 * @param value
	 *            值
	 */
	public void setLong(String key, long value) {
		setString(key, String.valueOf(value));
	}

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @param defaultValue
	 *            配置文件中找不到则返回默认值
	 * @return 返回的配置值
	 */
	public double getDouble(String key, double defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return Double.parseDouble(result);
	}

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @return 返回的配置值 找不到返回0
	 */
	public double getDouble(String key) {
		return getDouble(key, 0);
	}

	/**
	 * 设置配置值
	 * 
	 * @param key
	 *            配置项关键字
	 * @param value
	 *            值
	 */
	public void setDouble(String key, double value) {
		setString(key, String.valueOf(value));
	}

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @param defaultValue
	 *            配置文件中找不到则返回默认值
	 * @return 返回的配置值
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return Boolean.parseBoolean(result);
	}

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @return 返回的配置值 找不到配置返回false
	 */
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	/**
	 * 设置配置值
	 * 
	 * @param key
	 *            配置项关键字
	 * @param value
	 *            值
	 */
	public void setBoolean(String key, boolean value) {
		setString(key, String.valueOf(value));
	}

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @param defaultValue
	 *            配置文件中找不到则返回默认值
	 * @return 返回的配置值
	 */
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

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @return 返回的配置值 找不到返回0
	 */
	public Date getDate(String key) {
		return getDate(key, null);
	}

	/**
	 * 设置配置值
	 * 
	 * @param key
	 *            配置项关键字
	 * @param value
	 *            值
	 */
	public void setDate(String key, Date value) {
		if (value == null) {
			setString(key, "");
		}
		setString(key, dateFormat.format(value));
	}

	/**
	 * 保存配置文件，如果不存在，创建之
	 * 
	 * @return true表示保存成功
	 */
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