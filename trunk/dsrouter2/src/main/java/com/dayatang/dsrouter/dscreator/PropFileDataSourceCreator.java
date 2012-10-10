package com.dayatang.dsrouter.dscreator;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.utils.Configuration;
import com.dayatang.utils.ConfigurationFileImpl;

public class PropFileDataSourceCreator implements DataSourceCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropFileDataSourceCreator.class);
	private PoolType poolType;
	
	public PropFileDataSourceCreator() {
	}

	@Override
	public DataSource createDataSource(String tenant) {
		try {
			debug("Prepare to create Datasource for tenant {}, DB type is: {}, Pool type is: {}", tenant, poolType);
			return getPoolType().createDataSource(tenant);
		} catch (Exception e) {
			throw new DataSourceCreationException(e);
		}
	}

	public PoolType getPoolType() {
		if (poolType == null) {
			Configuration configuration = ConfigurationFileImpl.fromClasspath(Constants.DB_CONF_FILE);
			poolType = PoolType.valueOf(configuration.getString("pool.type"));
		}
		return poolType;
	}

	public void setPoolType(PoolType poolType) {
		this.poolType = poolType;
	}

	private void debug(String message, Object... params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, params);
		}
	}
}
