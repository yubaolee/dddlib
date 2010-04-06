package com.dayatang.commons.factory;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

import com.dayatang.commons.domain.InstanceProvider;

public class TapestryProvider implements InstanceProvider {
	private static Registry registry;

	public TapestryProvider(Registry registry) {
		TapestryProvider.registry = registry;
	}

	@SuppressWarnings("unchecked")
	public TapestryProvider(Class... beanClasses) {
		RegistryBuilder builder = new RegistryBuilder();
		builder.add(beanClasses);
		registry = builder.build();
		registry.performRegistryStartup();
	}

	@Override
	public <T> T getInstance(Class<T> beanClass) {
		return registry.getService(beanClass);
	}

	@Override
	public <T> T getInstance(Class<T> beanClass, String beanName) {
		return registry.getService(beanName, beanClass);
	}
}
