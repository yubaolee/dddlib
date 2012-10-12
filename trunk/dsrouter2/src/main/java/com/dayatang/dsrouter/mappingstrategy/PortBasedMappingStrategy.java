package com.dayatang.dsrouter.mappingstrategy;

import java.util.Properties;

public class PortBasedMappingStrategy extends AbstractDbMappingStrategy {

	public PortBasedMappingStrategy(DbMapper dbMapper) {
		super(dbMapper);
	}

	@Override
	public String getPort(String tenant, Properties properties) {
		return getDbMapper().getMappingValue(tenant);
	}

}
