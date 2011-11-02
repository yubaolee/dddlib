package com.dayatang.observer.integration;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;

import com.dayatang.domain.EntityRepository;
import com.dayatang.hibernate.EntityRepositoryHibernate;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class AppModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(EntityRepository.class, EntityRepositoryHibernate.class);

	}

	public static void contributeHibernateEntityPackageManager(
			Configuration<String> configuration) {
		configuration.add("com.dayatang.observer");
	}

	// @Match("*Repository")
	// public static <T> T decorateTransactionally(HibernateTransactionDecorator
	// decorator, Class<T> serviceInterface,
	// T delegate, String serviceId) {
	// return decorator.build(serviceInterface, delegate, serviceId);
	// }

}
