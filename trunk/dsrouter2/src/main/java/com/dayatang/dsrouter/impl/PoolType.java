package com.dayatang.dsrouter.impl;

import javax.sql.DataSource;

import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public enum PoolType {
	C3P0 {
		@Override
		public DataSource getDataSourceClass() throws InstantiationException,
				IllegalAccessException {
			ComboPooledDataSource result =  ComboPooledDataSource.class.newInstance();
			return result;
		}
	},
	PROXOOL {
		@Override
		public DataSource getDataSourceClass() throws InstantiationException,
				IllegalAccessException {
			return ProxoolDataSource.class.newInstance();
		}
	};

	public abstract DataSource getDataSourceClass()
			throws InstantiationException, IllegalAccessException;
}
