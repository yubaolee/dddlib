package com.dayatang.dsrouter.dscreator;

import javax.sql.DataSource;

import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.utils.Configuration;
import com.dayatang.utils.ConfigurationFileImpl;
import com.dayatang.utils.Slf4jLogger;

/**
 * 基于属性文件的数据源创建器实现。
 * @author yyang
 *
 */
public class ConnectionPoolDataSourceCreator implements DataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.of(ConnectionPoolDataSourceCreator.class);
	private ConnectionPoolType connectionPoolType;
	private Configuration configuration;
	
	public ConnectionPoolDataSourceCreator() {
	}

	@Override
	public DataSource createDataSourceForTenant(String tenant) {
		try {
			LOGGER.debug("Prepare to create Datasource for tenant {}, Pool type is: {}", tenant, connectionPoolType);
			return getPoolType().createDataSource(tenant);
		} catch (Exception e) {
			throw new DataSourceCreationException(e);
		}
	}

	public ConnectionPoolType getPoolType() {
		if (connectionPoolType == null) {
			connectionPoolType = ConnectionPoolType.valueOf(getConfiguration().getString(Constants.POOL_TYPE));
		}
		return connectionPoolType;
	}

	public void setPoolType(ConnectionPoolType connectionPoolType) {
		this.connectionPoolType = connectionPoolType;
	}

	public Configuration getConfiguration() {
		if (configuration == null) {
			configuration = ConfigurationFileImpl.fromClasspath(Constants.DB_CONF_FILE);
		}
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

}
