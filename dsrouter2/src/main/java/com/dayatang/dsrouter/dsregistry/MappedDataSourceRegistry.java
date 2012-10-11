package com.dayatang.dsrouter.dsregistry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.dayatang.dsrouter.DataSourceRegistry;
import com.dayatang.utils.Slf4jLogger;

/**
 * 用内存中的Map来保存、注册租户数据源的数据源注册表实现。
 * @author yyang
 *
 */
public class MappedDataSourceRegistry implements DataSourceRegistry {

	private static final Slf4jLogger LOGGER = Slf4jLogger.of(MappedDataSourceRegistry.class);
	
	private DataSourceCreator dataSourceCreator;
	private static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
	private static Map<String, Date> lastAccess = new HashMap<String, Date>();

	public void setDataSourceCreator(DataSourceCreator dataSourceCreator) {
		this.dataSourceCreator = dataSourceCreator;
	}
	
	@Override
	public DataSource getDataSourceOfTenant(String tenant) {
		lastAccess.put(tenant, new Date());
		DataSource result = dataSources.get(tenant);
		if (result != null) {
			return result;
		}
		synchronized (this) {
			if (!exists(tenant)) {
				result = dataSourceCreator.createDataSourceForTenant(tenant);
				dataSources.put(tenant, result);
				Date now = new Date();
				lastAccess.put(tenant, now);
				LOGGER.debug("Create data source for tenant '{}' at {}", tenant, now);
			}
		}
		return result;
	}

	public void registerDataSourceForTenant(String tenant, DataSource dataSource) {
		dataSources.put(tenant, dataSource);
	}

	public void releaseDataSourceOfTenant(String tenant) {
		DataSource dataSource = dataSources.remove(tenant);
		if (dataSource != null) {
			dataSource = null;
			LOGGER.debug("Data source of tenant '" + tenant + "' released!");
		}
		lastAccess.remove(tenant);
	}

	//Clear/release all cached DataSource.
	public void releaseAllDataSources() {
		dataSources.clear();
		lastAccess.clear();
		LOGGER.debug("All tenant datasource have been released!");
	}

	public int size() {
		return dataSources.size();
	}

	public boolean exists(String tenant) {
		return dataSources.containsKey(tenant);
	}

	/**
	 * @param tenant
	 * @return
	 */
	public Date getLastAccessTimeOfTenant(String tenant) {
		return lastAccess.get(tenant);
	}

}
