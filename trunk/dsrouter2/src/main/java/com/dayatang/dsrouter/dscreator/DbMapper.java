package com.dayatang.dsrouter.dscreator;

import org.apache.commons.lang3.StringUtils;

import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.utils.Configuration;
import com.dayatang.utils.ConfigurationFileImpl;

public class DbMapper {

	private Configuration configuration;
	
	public DbMapper() {
		configuration = ConfigurationFileImpl.fromClasspath(Constants.DB_MAPPING_FILE);
	}

	public String getProperty(String tenantId) {
		String result = configuration.getString(tenantId);
		if (StringUtils.isBlank(result)) {
			throw new DataSourceCreationException("There's not db mapping for tenant '" + tenantId + "'");
		}
		return result;
	}
	
	
}
