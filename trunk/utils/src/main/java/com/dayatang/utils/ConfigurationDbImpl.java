package com.dayatang.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <P>ConfigurationDbImpl为读取/回写配置信息的工具类，并将配置信息写入数据库， 具体配置大致采用
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
public class ConfigurationDbImpl implements WritableConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationDbImpl.class);
	
	private DataSource dataSource;
	private String tableName;
	private static final String keyColumn = "KEY_COLUMN";
	private static final String valueColumn = "VALUE_COLUMN";
	private String prefix = "";
	private Properties properties;
	
	
	public ConfigurationDbImpl(DataSource dataSource, String tableName) {
		super();
		this.dataSource = dataSource;
		this.tableName = tableName;
	}

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
		String result = (String) getProperties().get(key);
		if (result == null) {
			result = (String) getProperties().get(prefix + key);
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
			getProperties().remove(key);
			return;
		}
		getProperties().put(key, StringPropertyReplacer.replaceProperties(value));
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
		String dateAsLong = getString(key);
		if (StringUtils.isBlank(dateAsLong)) {
			return defaultValue;
		}
		return new Date(Long.parseLong(dateAsLong));
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

	@Override
	public Properties getProperties() {
		if (properties != null && !properties.isEmpty()) {
			return properties;
		}
		Connection connection = null;
		try {
			connection = getConnection(dataSource);
			createTableIfNotExists(connection);
			properties = executeSql("SELECT * FROM " + tableName, connection);
			debug("Configuration info loaded from table '{}'", tableName);
			return properties;
		} catch (SQLException e) {
			error("Access database failure!");
			throw new RuntimeException(e);
		}
		finally {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				error("Close database connection failure!");
				throw new RuntimeException(e);
			}
		}
	}

	//不同数据库的创建表语法不同，暂时希望用户在使用前先创建表
	private void createTableIfNotExists(Connection connection) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (KEY_COLUMN VARCHAR(255) PRIMARY KEY, VALUE_COLUMN VARCHAR(255))";
		executeSqlUpdate(sql, connection);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#save()
	 */
	@Override
	public void save() {
		Connection connection = null;
		try {
			connection = getConnection(dataSource);
			//String sql = String.format("DELETE FROM %s SET %s = ? WHERE %s = ?",  tableName, valueColumn, keyColumn);
			String sql = "TRUNCATE TABLE " + tableName;
			executeSqlUpdate(sql, connection);
			sql = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",  tableName, keyColumn, valueColumn);
			PreparedStatement stmt = connection.prepareStatement(sql);
			Properties properties = getProperties();
			for (Object key : properties.keySet()) {
				stmt.setString(1, (String) key);
				stmt.setString(2, properties.getProperty((String) key));
				stmt.executeUpdate();
			}
			stmt.close();
		} catch (SQLException e) {
			error("Access database failure!");
			throw new RuntimeException(e);
		}
		finally {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				error("Close database connection failure!");
				throw new RuntimeException(e);
			}
		}
	}

	private Connection getConnection(DataSource dataSource) throws SQLException {
		return dataSource.getConnection();
	}

	private int executeSqlUpdate(String sql, Connection connection) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(sql);
		int result = stmt.executeUpdate();
		stmt.close();
		return result;
	}

	private Properties executeSql(String sql, Connection connection) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		Properties results = new Properties();
		while (rs.next()) {
			results.put(rs.getString(keyColumn), rs.getString(valueColumn));
		}
		rs.close();
		stmt.close();
		return results;
	}
	

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	private static void debug(String message, Object... params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, params);
		}
	}

	private static void error(String message, Object... params) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message, params);
		}
	}
}