package com.dayatang.dsrouter.dscreator;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public enum DbType {
	MYSQL {
		@Override
		public String getJdbcUrl(String tenantId, Properties properties) {
			DbMappingStrategy mappingStrategy = DbMappingStrategy.valueOf(properties.getProperty(Constants.DB_MAPPING_STRATEGY));
			String result =  String.format("jdbc:mysql://%s:%s/%s", 
					mappingStrategy.getHost(tenantId, properties), 
					mappingStrategy.getPort(tenantId, properties), 
					mappingStrategy.getDbName(tenantId, properties));
			String extraUrlString = properties.getProperty(Constants.JDBC_EXTRA_URL_STRING);
			if (StringUtils.isNotBlank(extraUrlString)) {
				result = result + "?" + extraUrlString;
			}
			return result;
		}
	},
	ORACLE {
		@Override
		public String getJdbcUrl(String tenantId, Properties properties) {
			DbMappingStrategy mappingStrategy = DbMappingStrategy.valueOf(properties.getProperty(Constants.DB_MAPPING_STRATEGY));
			return String.format("jdbc:oracle:thin:@%s:%s:%s", 
					mappingStrategy.getHost(tenantId, properties), 
					mappingStrategy.getPort(tenantId, properties), 
					mappingStrategy.getInstanceName(tenantId, properties));
		}
	};

	public abstract String getJdbcUrl(String tenantId, Properties properties);
}

