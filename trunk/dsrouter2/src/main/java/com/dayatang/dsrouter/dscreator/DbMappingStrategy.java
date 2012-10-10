package com.dayatang.dsrouter.dscreator;

import java.util.Properties;

/**
 * 租户数据库映射策略。
 * @author yyang
 *
 */
public enum DbMappingStrategy {
	DB_NAME {

		@Override
		public String getDbName(String tenant, Properties properties) {
			return dbMapper.getMappingValue(tenant);
		}

	},
	HOST {

		@Override
		public String getHost(String tenant, Properties properties) {
			return dbMapper.getMappingValue(tenant);
		}

	},
	PORT {

		@Override
		public String getPort(String tenant, Properties properties) {
			return dbMapper.getMappingValue(tenant);
		}

	},
	SCHEMA {

		@Override
		public String getSchema(String tenant, Properties properties) {
			return dbMapper.getMappingValue(tenant);
		}

	},
	INSTANCE {

		@Override
		public String getInstanceName(String tenant, Properties properties) {
			return dbMapper.getMappingValue(tenant);
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
