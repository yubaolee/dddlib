package com.dayatang.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

public class DataSourceUtils {

	private static final Slf4jLogger LOGGER = Slf4jLogger
			.of(DataSourceUtils.class);

	public static Connection getConnection(DataSource dataSource) {
		try {
			return dataSource.getConnection();
		} catch (SQLException ex) {
			LOGGER.error("Could not close JDBC Connection", ex);
			throw new RuntimeException("Could not get JDBC Connection", ex);
		}
	}

	public static void releaseConnection(Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException ex) {
			LOGGER.error("Could not close JDBC Connection", ex);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected exception on closing JDBC Connection", ex);
		} finally {
			connection = null;
		}
	}

	public static List<String[]> executeQuery(DataSource dataSource, String sql) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String[]> results = new ArrayList<String[]>();
		try {
			connection = dataSource.getConnection();
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String[] row = new String[2];
				row[0] = rs.getString(0);
				row[1] = rs.getString(1);
				results.add(row);
			}
			return results;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close Resultset!");
					throw new RuntimeException(e);
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close query statement!");
					throw new RuntimeException(e);
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close connection!");
					throw new RuntimeException(e);
				}
			}
		}

	}

	public static int executeUpdate(DataSource dataSource, String sql) {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = dataSource.getConnection();
			stmt = connection.prepareStatement(sql);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close query statement!");
					throw new RuntimeException(e);
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close connection!");
					throw new RuntimeException(e);
				}
			}
		}

	}

	public static void executeUpdate(DataSource dataSource, Set<String> sqls) {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = dataSource.getConnection();
			for (String sql : sqls) {
				stmt = connection.prepareStatement(sql);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close query statement!");
					throw new RuntimeException(e);
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error("Could not close connection!");
					throw new RuntimeException(e);
				}
			}
		}

	}

}
