package com.dayatang.db;

import java.sql.Driver;

public abstract class AbstractDBManager implements DBManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5544581304658865155L;

	@SuppressWarnings("unused")
	private AbstractDBManager() {
	}

	public AbstractDBManager(String jdbcUrl, String username, String password,
			Class<Driver> driverClass) {
		// 属性均从PropertiesUtil中获取
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
		this.driverClass = driverClass;
	}

	protected String jdbcUrl;
	protected String username;
	protected String password;
	protected Class<Driver> driverClass;

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Class<Driver> getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(Class<Driver> driverClass) {
		this.driverClass = driverClass;
	}

}
