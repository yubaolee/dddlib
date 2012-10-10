package com.dayatang.dsrouter.dsregistry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.dsrouter.DataSourceRegistry;

public class MappedDataSourceRegistry implements DataSourceRegistry {

	private static final Logger LOGGER = LoggerFactory.getLogger(MappedDataSourceRegistry.class);
	
	private DataSourceCreator dataSourceCreator;
	private Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
	private Map<String, Date> lastAccess = new HashMap<String, Date>();

	public void setDataSourceCreator(DataSourceCreator dataSourceCreator) {
		this.dataSourceCreator = dataSourceCreator;
	}
	
	@Override
	public DataSource getOrCreateDataSourceByTenant(String tenant) {
		lastAccess.put(tenant, new Date());
		DataSource result = dataSources.get(tenant);
		if (result != null) {
			return result;
		}
		result = dataSourceCreator.createDataSource(tenant);
		dataSources.put(tenant, result);
		Date now = new Date();
		lastAccess.put(tenant, now);
		debug("Create data source for tenant '{}' at {}", tenant, now);
		return result;
	}

	public void registerTenantDataSource(String tenant, DataSource dataSource) {
		dataSources.put(tenant, dataSource);
	}

	//Clear/release all cached DataSource.
	public void releaseAllDataSources() {
		dataSources.clear();
		debug("All tenant datasource have been released!");
	}

	public int size() {
		return dataSources.size();
	}

	public void releaseDataSourceByTenant(String tenant) {
		DataSource dataSource = dataSources.remove(tenant);
		if (dataSource != null) {
			dataSource = null;
			debug("Data source of tenant '" + tenant + "' released!");
		}
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

	private void debug(String message, Object... params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, params);
		}
	}
}
