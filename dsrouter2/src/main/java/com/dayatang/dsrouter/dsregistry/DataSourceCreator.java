package com.dayatang.dsrouter.dsregistry;

import javax.sql.DataSource;

public interface DataSourceCreator {

	DataSource createDataSource(String tenantId);

}
