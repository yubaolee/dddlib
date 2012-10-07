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
			debug("准备为租户‘{}’构建数据源，数据库类型是{}，连接池类型是{}，属性=【{}】", tenantId, dbType, poolType, configuration.getProperties());
			return poolType.getDataSourceClass();
		} catch (InstantiationException e) {
			throw new DataSourceCreationException(e);
		} catch (IllegalAccessException e) {
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
