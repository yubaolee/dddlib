package com.dayatang.dsrouter.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.dsrouter.context.memory.ContextHolder;

public class SimpleDynamicRoutingDataSource extends AbstractDataSource {

	private static final Logger logger = LoggerFactory
			.getLogger(SimpleDynamicRoutingDataSource.class);

	private DataSource defaultDataSource;

	private Map<String, DataSource> dataSourceMapping = new HashMap<String, DataSource>();

	public DataSource getDefaultDataSource() {
		return defaultDataSource;
	}

	public void setDefaultDataSource(DataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	public Map<String, DataSource> getDataSourceMapping() {
		return dataSourceMapping;
	}

	public void setDataSourceMapping(Map<String, DataSource> dataSourceMapping) {
		this.dataSourceMapping = dataSourceMapping;
	}

	public void afterPropertiesSet() throws Exception {
		if (defaultDataSource != null) {

			if (logger.isDebugEnabled()) {
				logger.debug("设置默认数据源【{}】", defaultDataSource);
			}

		}
	}

	protected DataSource determineTargetDataSource() {
		try {
			DataSource ds = getDataSource();
			if (ds == null) {
				ds = defaultDataSource;
			}
			return ds;
		} catch (Exception ex) {
			if (logger.isWarnEnabled()) {
				logger.warn("获取数据源发生异常，使用默认数据源【{}】", defaultDataSource);
				logger.warn("异常为：" + ex);
			}
			return defaultDataSource;
		}

	}

	protected DataSource getDataSource() {
		String dataSourceKey = ContextHolder.getContextType();

		DataSource dataSource = dataSourceMapping.get(dataSourceKey);

		if (dataSource == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("系统中不存在 dataSourceKey=【{}】对应的数据源，使用默认数据源【{}】",
						dataSourceKey, defaultDataSource);
			}
			dataSource = defaultDataSource;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("系统中已存在 dataSourceKey=【{}】对应的数据源【{}】。",
						dataSourceKey, dataSource);
			}
		}

		return dataSource;
	}

	public Connection getConnection() throws SQLException {
		DataSource ds = (DataSource) determineTargetDataSource();
		Connection connection = ds.getConnection();
		if (logger.isDebugEnabled()) {
			// 注意：当使用c3p0的时候 不能在此处调用conn.getMetaData()方法 否则读写分离失效
			// logger.debug("获取的连接对象为【{}】", connection.getMetaData().getURL());
		}
		return connection;
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		Connection connection = determineTargetDataSource().getConnection(
				username, password);
		return connection;
	}

}
