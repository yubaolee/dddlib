package com.dayatang.dsrouter;

import javax.sql.DataSource;

/**
 * 租户数据源注册表
 * @author yyang
 *
 */
public interface DataSourceRegistry {

	DataSource getOrCreateDataSourceByTenant(String tenant);

}
