package com.dayatang.dsrouter.dscreator;

import org.apache.commons.lang3.StringUtils;

import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.utils.Configuration;
import com.dayatang.utils.ConfigurationFileImpl;

/**
 * 租户数据库映射器。读取tenant-db-mapping.properties中的映射数据。
 * @author yyang
 *
 */
public class DbMapper {

	private Configuration configuration;
	
	public DbMapper() {
		configuration = ConfigurationFileImpl.fromClasspath(Constants.DB_MAPPING_FILE);
	}

	public String getMappingValue(String tenant) {
		String result = configuration.getString(tenant);
		if (StringUtils.isBlank(result)) {
			throw new DataSourceCreationException("There's not db mapping for tenant '" + tenant + "'");
		}
		return result;
	}
	
	
}
