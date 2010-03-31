package com.dayatang.commons.querychannel.integration;

import org.apache.tapestry5.hibernate.HibernateTransactionDecorator;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.annotations.Match;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class AppModule {

	public static void contributeHibernateEntityPackageManager(Configuration<String> configuration) {
		configuration.add("com.dayatang.hrm.querychannel.domain");
	}

	@Match("*Repository")
	public static <T> T decorateTransactionally(
			HibernateTransactionDecorator decorator, Class<T> serviceInterface,
			T delegate, String serviceId) {
		return decorator.build(serviceInterface, delegate, serviceId);
	}
}
