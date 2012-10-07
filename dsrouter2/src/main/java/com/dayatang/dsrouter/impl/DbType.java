package com.dayatang.dsrouter.impl;

import java.util.Properties;

public enum DbType {
	MYSQL {
		@Override
		public String getJdbcUrl(String tenantId, Properties properties) {
			// TODO 自动生成的方法存根
			return null;
		}
	},
	ORACLE {
		@Override
		public String getJdbcUrl(String tenantId, Properties properties) {
			// TODO 自动生成的方法存根
			return null;
		}
	};

	public abstract String getJdbcUrl(String tenantId, Properties properties);
}
