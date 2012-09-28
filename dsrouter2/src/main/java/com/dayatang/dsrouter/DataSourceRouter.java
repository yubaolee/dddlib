package com.dayatang.dsrouter;

import javax.sql.DataSource;

public interface DataSourceRouter {

	DataSource getOrCreateDataSource();

}
