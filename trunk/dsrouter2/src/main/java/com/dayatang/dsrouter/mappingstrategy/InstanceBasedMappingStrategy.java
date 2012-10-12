package com.dayatang.dsrouter.mappingstrategy;

import java.util.Properties;

public class InstanceBasedMappingStrategy extends AbstractDbMappingStrategy {

	public InstanceBasedMappingStrategy(DbMapper dbMapper) {
		super(dbMapper);
	}

	@Override
	public String getInstanceName(String tenant, Properties properties) {
		return getDbMapper().getMappingValue(tenant);
	}

}
