package com.dayatang.db;

import java.sql.Driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chencao
 *
 */
public abstract class AbstractDBManager implements DBManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5544581304658865155L;

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractDBManager.class);


	@SuppressWarnings("unchecked")
	public AbstractDBManager() {
		this.jdbcUrl = PropertiesUtil.JDBC_URL;
		this.username = PropertiesUtil.JDBC_USERNAME;
		this.password = PropertiesUtil.JDBC_PASSWD;
		try {
			this.driverClass = (Class<Driver>) Class
					.forName(PropertiesUtil.JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			logger.error("initial driver class error!!");
			e.printStackTrace();
			System.exit(1);
		}
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
