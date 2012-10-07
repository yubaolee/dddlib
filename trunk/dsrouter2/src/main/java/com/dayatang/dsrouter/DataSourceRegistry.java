package com.dayatang.dsrouter;

import javax.sql.DataSource;

public interface DataSourceRegistry {

	DataSource getOrCreateDataSourceByTenantId(String tenantId);

}
