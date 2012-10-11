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
public class PropFileDataSourceCreator implements DataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.of(PropFileDataSourceCreator.class);
	private PoolType poolType;
	
	public PropFileDataSourceCreator() {
	}

	@Override
	public DataSource createDataSourceForTenant(String tenant) {
		try {
			LOGGER.debug("Prepare to create Datasource for tenant {}, DB type is: {}, Pool type is: {}", tenant, poolType);
			return getPoolType().createDataSource(tenant);
		} catch (Exception e) {
			throw new DataSourceCreationException(e);
		}
	}

	public PoolType getPoolType() {
		if (poolType == null) {
			Configuration configuration = ConfigurationFileImpl.fromClasspath(Constants.DB_CONF_FILE);
			poolType = PoolType.valueOf(configuration.getString(Constants.POOL_TYPE));
		}
		return poolType;
	}

	public void setPoolType(PoolType poolType) {
		this.poolType = poolType;
	}

}
