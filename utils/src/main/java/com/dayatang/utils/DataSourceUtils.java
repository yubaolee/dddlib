package com.dayatang.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceUtils.class);
	
	public static Connection getConnection(DataSource dataSource) {
		try {
			return dataSource.getConnection();
		}
		catch (SQLException ex) {
			error("Could not close JDBC Connection", ex);
			throw new RuntimeException("Could not get JDBC Connection", ex);
		}
	}

	public static void releaseConnection(Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
		catch (SQLException ex) {
			error("Could not close JDBC Connection", ex);
		}
		catch (Throwable ex) {
			error("Unexpected exception on closing JDBC Connection", ex);
		} finally {
			connection = null;
		}
	}

	private static void error(String message, Object... params) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message, params);
		}
	}

}
