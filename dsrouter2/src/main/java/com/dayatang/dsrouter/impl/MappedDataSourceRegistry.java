package com.dayatang.dsrouter.impl;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.dayatang.dsrouter.DataSourceRegistry;

public class MappedDataSourceRegistry implements DataSourceRegistry {

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
	}

	public int size() {
		return dataSources.size();
	}

}
