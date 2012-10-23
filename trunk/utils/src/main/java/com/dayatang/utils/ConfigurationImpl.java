package com.dayatang.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

public class ConfigurationImpl implements Configuration {
	
	private static final Slf4jLogger LOGGER = Slf4jLogger.of(ConfigurationImpl.class);
	
	private String prefix = "";
	
	private Properties properties = new Properties();

	/**
	 * 激活配置前缀功能
	 * 
	 * @param prefix 如"com.dayatang.mes."
	 */
	public void usePrefix(final String prefix) {
		if (StringUtils.isNotBlank(prefix)) {
			this.prefix = prefix.endsWith(".") ? prefix : prefix + ".";
		}
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getString(java.lang.String, java.lang.String)
	 */
	@Override
	public String getString(String key, String defaultValue) {
		Assert.notBlank(key, "Key is null or empty!");
		String result = (String) properties.get(key);
		if (result == null) {
			result = (String) properties.get(prefix + key);
		}
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
		Assert.notBlank(key, "Key is null or empty!");
		if (StringUtils.isBlank(value)) {
			properties.remove(key);
			return;
		}
		properties.put(key, StringPropertyReplacer.replaceProperties(value));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getInt(java.lang.String, int)
	 */
	@Override
	public int getInt(String key, int defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return StringUtils.isBlank(result) ? defaultValue : Integer.parseInt(result);
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
		return StringUtils.isBlank(result) ? defaultValue : Long.parseLong(result);
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
		return StringUtils.isBlank(result) ? defaultValue : Double.parseDouble(result);
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
		return StringUtils.isBlank(result) ? defaultValue : Boolean.parseBoolean(result);
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
		return StringUtils.isBlank(result) ? defaultValue : new Date(Long.parseLong(result));
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
		setString(key, String.valueOf(value.getTime()));
	}

	public void readFromFileSystem(final String dirPath, final String fileName) {
		Assert.notBlank(dirPath, String.format("Directory %s is empty!", dirPath));
		Assert.notBlank(fileName, String.format("File name %s is empty!", fileName));
		readFromFileSystem(new File(dirPath, fileName));
	}
	
	public void readFromFileSystem(final String pathname) {
		Assert.notBlank(pathname, String.format("Path name %s is empty!", pathname));
		readFromFileSystem(new File(pathname));
	}
	
	
	public void readFromFileSystem(File file) {
		Assert.notNull(file, String.format("File %s is Null!", file));
		if (!file.exists()) {
			throw new IllegalArgumentException(String.format("File $s not exists!", file.getAbsolutePath()));
		}
		if (!file.canRead()) {
			throw new IllegalStateException("File " + file.getName() + " is unreadable!");
		}
		try {
			readFromUrl(file.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public void readFromUrl(URL url) {
		Assert.notNull(url, String.format("Url %s is Null!", url));
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = url.openStream();
			props.load(in);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open input stream or read from url!", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("Cannot close input stream!", e);
			}
		}
	}

	public void readFromDatabase(DataSource dataSource) {
		ConfigurationDbUtils dbUtils = new ConfigurationDbUtils(dataSource);
		properties = dbUtils.readProperties();
	}

	public void readFromDatabase(DataSource dataSource, String tableName) {
		ConfigurationDbUtils dbUtils = new ConfigurationDbUtils(dataSource, tableName);
		properties = dbUtils.readProperties();
	}

	public void readFromDatabase(DataSource dataSource, String tableName, String keyColumn, String valueColumn) {
		ConfigurationDbUtils dbUtils = new ConfigurationDbUtils(dataSource, tableName, keyColumn, valueColumn);
		properties = dbUtils.readProperties();
	}

	public void saveToFile(File file) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public Properties getProperties() {
		return properties;
	}
}
