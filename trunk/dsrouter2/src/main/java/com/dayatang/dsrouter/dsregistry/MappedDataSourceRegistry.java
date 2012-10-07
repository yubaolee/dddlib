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
	public DataSource getOrCreateDataSourceByTenantId(String tenantId) {
		DataSource result = dataSources.get(tenantId);
		if (result == null) {
			result = dataSourceCreator.createDataSource(tenantId);
			dataSources.put(tenantId, result);
		}
		lastAccess.put(tenantId, new Date());
		return result;
	}

	//Clear/release all cached DataSource.
	public void releaseAllDataSources() {
		dataSources.clear();
		debug("All tenant datasource have been released!");
	}

	public int size() {
		return dataSources.size();
	}

	public void releaseDataSourceByTenantId(String tenantId) {
		DataSource dataSource = dataSources.remove(tenantId);
		if (dataSource != null) {
			dataSource = null;
			debug("Data source of tenant '" + tenantId + "' released!");
		}
	}

	public boolean exists(String tenantId) {
		return dataSources.containsKey(tenantId);
	}

	/**
	 * �⻧��������ݿ�ʱ�䡣��¼���ʱ����Ϊ�˿��Ը��ٳ�ʱ��û����ݿ���ʵ��⻧���Ա��ڱ�Ҫʱ������������Դ����ʡ��������Դ��
	 * @param tenantId
	 * @return
	 */
	public Date getLastAccessTimeOfTenant(String tenantId) {
		return lastAccess.get(tenantId);
	}

	private void debug(String message, Object... params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, params);
		}
	}
}
