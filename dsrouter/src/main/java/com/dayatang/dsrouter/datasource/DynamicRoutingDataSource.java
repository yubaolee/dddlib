package com.dayatang.dsrouter.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.dsrouter.builder.DataSourceBuilder;
import com.dayatang.dsrouter.builder.mysql.C3P0MySQLDataSourceBuilder;
import com.dayatang.dsrouter.context.DataSourceContext;

public class DynamicRoutingDataSource extends AbstractDataSource {

	private static final Logger logger = LoggerFactory
			.getLogger(DynamicRoutingDataSource.class);

	private DataSourceContext dataSourceContext;

	private DataSourceBuilder dataSourceBuilder;

	private DataSource defaultDataSource;

	private String defaultDataSourceKey;

	private Map<String, DataSource> dataSourceMapping = new HashMap<String, DataSource>();

	public DataSourceContext getDataSourceContext() {
		return dataSourceContext;
	}

	public void setDataSourceContext(DataSourceContext dataSourceContext) {
		this.dataSourceContext = dataSourceContext;
	}

	public DataSource getDefaultDataSource() {
		return defaultDataSource;
	}

	public void setDefaultDataSource(DataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	public DataSourceBuilder getDataSourceBuilder() {
		if (dataSourceBuilder == null) {
			if (logger.isInfoEnabled()) {
				logger.info("没有指定dataSourceBuilder属性，采用默认的数据源构造器：{}",
						C3P0MySQLDataSourceBuilder.class.getName());
			}
			this.dataSourceBuilder = new C3P0MySQLDataSourceBuilder();
		}
		return dataSourceBuilder;
	}

	public void setDataSourceBuilder(DataSourceBuilder dataSourceBuilder) {
		this.dataSourceBuilder = dataSourceBuilder;
	}

	public String getDefaultDataSourceKey() {
		return defaultDataSourceKey;
	}

	public void setDefaultDataSourceKey(String defaultDataSourceKey) {
		this.defaultDataSourceKey = defaultDataSourceKey;
	}

	public Map<String, DataSource> getDataSourceMapping() {
		return dataSourceMapping;
	}

	public void setDataSourceMapping(Map<String, DataSource> dataSourceMapping) {
		this.dataSourceMapping = dataSourceMapping;
	}

	// @Override
	public void afterPropertiesSet() throws Exception {
		if (defaultDataSource != null) {

			if (!StringUtils.isBlank(defaultDataSourceKey)) {
				dataSourceMapping.put(defaultDataSourceKey, defaultDataSource);

				if (logger.isDebugEnabled()) {
					logger.debug("设置默认数据源的key为：【{}】", defaultDataSourceKey);
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("设置默认数据源【{}】", defaultDataSource);
				}
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
		String url = dataSourceContext.getUrl();
		if (url == null) {
			// throw new RuntimeException("数据库URL不能为空。");
			return null;
		}
		DataSource dataSource = dataSourceMapping.get(url);

		if (dataSource == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("系统中不存在【{}】对应的数据源，需要重新构建。",
						dataSourceContext.getUrl());
			}
			dataSource = getDataSourceBuilder().buildDataSource(
					dataSourceContext.getUrl(),
					dataSourceContext.getUsername(),
					dataSourceContext.getPassword(),
					dataSourceContext.getProperties());

			dataSourceMapping.put(dataSourceContext.getUrl(), dataSource);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("系统中已存在【{}】对应的数据源【{}】。",
						dataSourceContext.getUrl(), dataSource);
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
		return buildCatalog(connection);
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		Connection connection = determineTargetDataSource().getConnection(
				username, password);
		return buildCatalog(connection);
	}

	protected Connection buildCatalog(Connection connection)
			throws SQLException {

		if (connection == null) {
			return null;
		}

		if (!StringUtils.isBlank(dataSourceContext.getSchema())) {
			if (logger.isDebugEnabled()) {
				logger.debug("设置连接对象的catalog为【{}】。",
						dataSourceContext.getSchema());
			}
			connection.setCatalog(dataSourceContext.getSchema());
		} else {
			// do nothing
		}

		return connection;

	}

}
