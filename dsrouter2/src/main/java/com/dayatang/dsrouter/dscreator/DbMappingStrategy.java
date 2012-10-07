package com.dayatang.dsrouter.dscreator;

import java.util.Properties;

public enum DbMappingStrategy {
	DB_NAME {

		@Override
		public String getDbName(String tenantId, Properties properties) {
			return dbMapper.getProperty(tenantId);
		}

	},
	HOST {

		@Override
		public String getHost(String tenantId, Properties properties) {
			return dbMapper.getProperty(tenantId);
		}

	},
	PORT {

		@Override
		public String getPort(String tenantId, Properties properties) {
			return dbMapper.getProperty(tenantId);
		}

	},
	SCHEMA {

		@Override
		public String getSchema(String tenantId, Properties properties) {
			return dbMapper.getProperty(tenantId);
		}

	},
	INSTANCE {

		@Override
		public String getInstanceName(String tenantId, Properties properties) {
			return dbMapper.getProperty(tenantId);
		}

	};

	private static DbMapper dbMapper = createDbMapper();

	public String getPort(String tenantId, Properties properties) {
		return properties.getProperty(Constants.JDBC_PORT);
	}

	private static DbMapper createDbMapper() {
		return new DbMapper();
	}

	public String getDbName(String tenantId, Properties properties) {
		return properties.getProperty(Constants.JDBC_DB_NAME);
	}

	public String getHost(String tenantId, Properties properties) {
		return properties.getProperty(Constants.JDBC_HOST);
	}

	public String getSchema(String tenantId, Properties properties) {
		return properties.getProperty(Constants.JDBC_SCHEMA);
	}

	public String getInstanceName(String tenantId, Properties properties) {
		return properties.getProperty(Constants.JDBC_INSTANCE);
	}
}
