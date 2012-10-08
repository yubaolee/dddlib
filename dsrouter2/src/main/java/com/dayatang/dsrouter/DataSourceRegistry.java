package com.dayatang.dsrouter;

import javax.sql.DataSource;

public interface DataSourceRegistry {

	DataSource getOrCreateDataSourceByTenant(String tenant);

}
