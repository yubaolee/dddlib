package com.dayatang.dsrouter.dscreator;

import com.dayatang.utils.Configuration;
import com.dayatang.utils.ConfigurationFileImpl;

public class DbMapper {

	private Configuration configuration;
	
	public DbMapper() {
		configuration = ConfigurationFileImpl.fromClasspath(Constants.DB_MAPPING_FILE);
	}

	public String getProperty(String tenantId) {
		return configuration.getString(tenantId);
	}
	
	
}
