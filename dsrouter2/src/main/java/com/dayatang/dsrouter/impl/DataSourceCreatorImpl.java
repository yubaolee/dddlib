package com.dayatang.dsrouter.impl;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.utils.Configuration;
import com.dayatang.utils.ConfigurationFileImpl;

public class DataSourceCreatorImpl implements DataSourceCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceCreatorImpl.class);
	private DbType dbType;
	private PoolType poolType;
	private Configuration configuration = ConfigurationFileImpl.fromClasspath("/jdbc.properties");
	
	public DataSourceCreatorImpl() {
		dbType = DbType.valueOf(configuration.getString("db.type"));
		poolType = PoolType.valueOf(configuration.getString("pool.type"));
	}

	@Override
	public DataSource createDataSource(String tenantId) {
		try {
			debug("Prepare to create Datasource for tenant {}, DB type is: {}, Pool type is: {} properties is: {}", tenantId, dbType, poolType, configuration.getProperties());
			return poolType.createDataSource(tenantId);
		} catch (Exception e) {
			throw new DataSourceCreationException(e);
		}
	}

	public DbType getDbType() {
		return dbType;
	}

	public PoolType getPoolType() {
		return poolType;
	}

	private void debug(String message, Object... params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, params);
		}
	}
}
