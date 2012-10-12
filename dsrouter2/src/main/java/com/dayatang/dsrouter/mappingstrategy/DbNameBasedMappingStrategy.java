package com.dayatang.dsrouter.mappingstrategy;

import java.util.Properties;

public class DbNameBasedMappingStrategy extends AbstractDbMappingStrategy {

	public DbNameBasedMappingStrategy(DbMapper dbMapper) {
		super(dbMapper);
	}

	@Override
	public String getDbName(String tenant, Properties properties) {
		return getDbMapper().getMappingValue(tenant);
	}

}
