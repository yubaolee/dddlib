package com.dayatang.dsrouter.dsregistry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.dayatang.dsrouter.DataSourceRegistry;
import com.dayatang.utils.Assert;
import com.dayatang.utils.Slf4jLogger;

/**
 * 数据源注册表实现，将租户数据源映射到JNDI
 * @author yyang
 *
 */
public class JndiMappingDataSourceRegistry implements DataSourceRegistry {

	private static final Slf4jLogger LOGGER = Slf4jLogger.getLogger(JndiMappingDataSourceRegistry.class);
	private static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
	private static Map<String, Date> lastAccess = new HashMap<String, Date>();
	private Context context;
	

	public JndiMappingDataSourceRegistry() {
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			throw new RuntimeException("Cannot initiate JNDI environment!", e);
		}
	}

	@Override
	public DataSource getDataSourceOfTenant(String tenant) {
		Assert.notNull(tenant, "Tenant is null!");
		recordLastAccessTimeOfTenant(tenant, new Date());
		DataSource result = dataSources.get(tenant);
		if (result == null) {
			throw new RuntimeException("There's no data source prepared for tenant " + tenant);
		}
		return result;
	}

	private void recordLastAccessTimeOfTenant(String tenant, Date accessTime) {
		lastAccess.put(tenant, accessTime);
	}

	public void registerDataSourceForTenant(String tenant, String dataSourceJndi) {
		DataSource dataSource;
		try {
			dataSource = (DataSource) context.lookup(dataSourceJndi);
		} catch (NamingException e) {
			throw new RuntimeException("Lookup jndi: " + dataSourceJndi + " failed!", e);
		}
		if (dataSource == null) {
			throw new RuntimeException("There's no data source prepared for tenant " + tenant);
		}
		dataSources.put(tenant, dataSource);
	}

	public synchronized void releaseDataSourceOfTenant(String tenant) {
		DataSource dataSource = dataSources.remove(tenant);
		if (dataSource != null) {
			dataSource = null;
			LOGGER.debug("Data source of tenant '" + tenant + "' released!");
		}
		lastAccess.remove(tenant);
	}

	//Clear/release all cached DataSource.
	public synchronized void releaseAllDataSources() {
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
	 * 获取每个租户的最后访问时间。
	 * @param tenant
	 * @return
	 */
	public Date getLastAccessTimeOfTenant(String tenant) {
		return lastAccess.get(tenant);
	}

}
