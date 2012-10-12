package com.dayatang.dsrouter.mappingstrategy;

import java.util.Properties;

public class HostBasedMappingStrategy extends AbstractDbMappingStrategy {

	public HostBasedMappingStrategy(DbMapper dbMapper) {
		super(dbMapper);
	}

	@Override
	public String getHost(String tenant, Properties properties) {
		return getDbMapper().getMappingValue(tenant);
	}

}
