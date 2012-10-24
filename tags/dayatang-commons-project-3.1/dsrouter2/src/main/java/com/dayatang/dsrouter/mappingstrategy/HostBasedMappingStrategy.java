package com.dayatang.dsrouter.mappingstrategy;

import java.util.Properties;

import com.dayatang.utils.Configuration;

public class HostBasedMappingStrategy extends AbstractDbMappingStrategy {

	public HostBasedMappingStrategy(Configuration configuration) {
		super(configuration);
	}

	@Override
	public String getHost(String tenant, Properties properties) {
		return getConfiguration().getString(tenant);
	}

}
