package com.dayatang.dsrouter.impl;

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

	public void setDataSourceCreator(DataSourceCreator dataSourceCreator) {
		this.dataSourceCreator = dataSourceCreator;
	}
	
	@Override
	public DataSource getOrCreateDataSourceByTenantId(String tenantId) {
		DataSource result = dataSources.get(tenantId);
		if (result == null) {
			result = dataSourceCreator.createDataSource(tenantId);
		}
		dataSources.put(tenantId, result);
		return result;
	}

	//Clear/release all cached DataSource.
	public void clear() {
		dataSources.clear();
		debug("All tenant datasource have been released!");
	}

	public int size() {
		return dataSources.size();
	}

	private void debug(String message, Object... params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, params);
		}
	}
}
