package com.dayatang.dsrouter.mappingstrategy;

import java.util.Properties;

public class SchemaBasedMappingStrategy extends AbstractDbMappingStrategy {

	public SchemaBasedMappingStrategy(DbMapper dbMapper) {
		super(dbMapper);
	}

	@Override
	public String getSchema(String tenant, Properties properties) {
		return getDbMapper().getMappingValue(tenant);
	}

}
