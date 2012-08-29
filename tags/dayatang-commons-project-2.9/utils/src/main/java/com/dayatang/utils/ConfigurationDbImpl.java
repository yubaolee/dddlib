package com.dayatang.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

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
public class ConfigurationDbImpl extends AbstractConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationDbImpl.class);
	
	private DataSource dataSource;
	private String tableName = "SYS_CONFIG";
	private static final String keyColumn = "KEY_COLUMN";
	private static final String valueColumn = "VALUE_COLUMN";
	private Hashtable<String, String> hTable;	
	
	
	public ConfigurationDbImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ConfigurationDbImpl(DataSource dataSource, String tableName) {
		this.dataSource = dataSource;
		this.tableName = tableName;
	}

	//不同数据库的创建表语法不同，暂时希望用户在使用前先创建表
	private void createTableIfNotExists(Connection connection) {
		String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (KEY_COLUMN VARCHAR(255) PRIMARY KEY, VALUE_COLUMN VARCHAR(255))";
		try {
			connection.setAutoCommit(false);
			executeSqlUpdate(sql, connection);
			connection.commit();
		} catch (SQLException e) {
			//connection.setReadOnly(true);
			error("Could not create configurarion table", e);
			throw new RuntimeException("Could not create configurarion table", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#save()
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void save() {
		Connection connection = DataSourceUtils.getConnection(dataSource);
		try {
			connection.setAutoCommit(false);
			PreparedStatement queryStmt = connection.prepareStatement(String.format("SELECT * FROM %s", tableName));
			PreparedStatement updateStmt = connection.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE %s = ?", tableName, valueColumn, keyColumn));
			PreparedStatement insertStmt = connection.prepareStatement(String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",  tableName, keyColumn, valueColumn));
			ResultSet rs = queryStmt.executeQuery();
			Set<String> keys = new HashSet(hTable.keySet());
			while (rs.next()) {
				String key = rs.getString(keyColumn);
				if (keys.contains(key)) {
					updateStmt.setString(1, hTable.get(key));
					updateStmt.setString(2, key);
					updateStmt.executeUpdate();
					keys.remove(key);
				}
			}
			for (String key : keys) {
				insertStmt.setString(1, key);
				insertStmt.setString(2, hTable.get(key));
				insertStmt.executeUpdate();
			}
			rs.close();
			queryStmt.close();
			updateStmt.close();
			insertStmt.close();
			connection.commit();
		} catch (SQLException e) {
			error("save configuration to database failure!");
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RuntimeException(e1);
			}
			throw new RuntimeException(e);
		}
		DataSourceUtils.releaseConnection(connection);
	}

	private int executeSqlUpdate(String sql, Connection connection) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(sql);
		int result = stmt.executeUpdate();
		stmt.close();
		return result;
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

	@Override
	public Hashtable<String, String> getHashtable() {
		if (hTable == null) {
			Connection connection = DataSourceUtils.getConnection(dataSource);
			createTableIfNotExists(connection);
			refresh();
			DataSourceUtils.releaseConnection(connection);
		}
		return hTable;
	}

	//从数据库中取得配置项，更新当前内存中的配置值。
	public void refresh() {
		hTable = new Hashtable<String, String>();
		Connection connection = DataSourceUtils.getConnection(dataSource);
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("SELECT * FROM " + tableName);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				hTable.put(rs.getString(keyColumn), rs.getString(valueColumn));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			error("Read configuration from database failure!");
			throw new RuntimeException("Read configuration from database failure!", e);
		}
		DataSourceUtils.releaseConnection(connection);
		debug("Configuration info loaded from table '{}'", tableName);
	}

	@Override
	public Properties getProperties() {
		Properties results = new Properties();
		for (Map.Entry<String, String> each : hTable.entrySet()) {
			results.put(each.getKey(), each.getValue());
		}
		return results;
	}
}