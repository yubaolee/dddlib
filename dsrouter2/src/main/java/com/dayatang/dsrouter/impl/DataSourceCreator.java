package com.dayatang.dsrouter.impl;

import javax.sql.DataSource;

public interface DataSourceCreator {

	DataSource createDataSource(String tenantId);

}
