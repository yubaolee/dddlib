package com.dayatang.dsrouter.dscreator;

import java.util.Properties;

public enum DbMappingStrategy {
	DB_NAME {

		@Override
		public String getDbName(String tenant, Properties properties) {
			return dbMapper.getProperty(tenant);
		}

	},
	HOST {

		@Override
		public String getHost(String tenant, Properties properties) {
			return dbMapper.getProperty(tenant);
		}

	},
	PORT {

		@Override
		public String getPort(String tenant, Properties properties) {
			return dbMapper.getProperty(tenant);
		}

	},
	SCHEMA {

		@Override
		public String getSchema(String tenant, Properties properties) {
			return dbMapper.getProperty(tenant);
		}

	},
	INSTANCE {

		@Override
		public String getInstanceName(String tenant, Properties properties) {
			return dbMapper.getProperty(tenant);
		}

	};

	private static DbMapper dbMapper = createDbMapper();

	public String getPort(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_PORT);
	}

	private static DbMapper createDbMapper() {
		return new DbMapper();
	}

	public String getDbName(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_DB_NAME);
	}

	public String getHost(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_HOST);
	}

	public String getSchema(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_SCHEMA);
	}

	public String getInstanceName(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_INSTANCE);
	}
}
