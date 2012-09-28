package com.dayatang.dsrouter;

import javax.sql.DataSource;

public class DataSourceFactory {
	
	private DataSourceRouter router;
	
	public DataSourceFactory(DataSourceRouter router) {
		super();
		this.router = router;
	}

	public DataSource createDataSource() {
		return router.getOrCreateDataSource();
	}

}
