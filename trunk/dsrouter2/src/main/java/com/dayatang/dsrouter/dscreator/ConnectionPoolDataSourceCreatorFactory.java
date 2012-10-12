package com.dayatang.dsrouter.dscreator;

import org.apache.commons.lang3.StringUtils;

import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.utils.Configuration;
import com.dayatang.utils.ConfigurationFileImpl;

/**
 * 连接池数据源创建器工厂。
 * @author yyang
 *
 */
public class ConnectionPoolDataSourceCreatorFactory {
	
	private static final String C3P0 = "C3P0";
	private static final String PROXOOL = "PROXOOL";
	private static final String COMMONS_DBCP = "COMMONS_DBCP";
	private Configuration configuration;
	
	public DataSourceCreator getInstance() {
		String connectionPoolType = getConfiguration().getString(Constants.CONNECTION_POOL_TYPE);
		if (StringUtils.isBlank(connectionPoolType)) {
			throw new DataSourceCreationException("connection.pool.type not setted!");
		}
		if (connectionPoolType.equalsIgnoreCase(C3P0)) {
			return new C3P0DataSourceCreator(configuration.getProperties());
		}
		if (connectionPoolType.equalsIgnoreCase(PROXOOL)) {
			return new ProxoolDataSourceCreator(configuration.getProperties());
		}
		if (connectionPoolType.equalsIgnoreCase(COMMONS_DBCP)) {
			return new CommonsDbcpDataSourceCreator(configuration.getProperties());
		}
		throw new DataSourceCreationException("Unsupported connection pool type");
	}
	
	public ConnectionPoolDataSourceCreatorFactory() {
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
