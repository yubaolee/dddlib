package com.dayatang.jdk6provider;

import java.util.ServiceLoader;

import com.dayatang.domain.InstanceProvider;

public class Jdk6Provider implements InstanceProvider {

	@Override
	public <T> T getInstance(Class<T> beanClass) {
		return ServiceLoader.load(beanClass).iterator().next();
	}

	/**
	 * 本实现暂不支持此操作
	 */
	@Override
	public <T> T getInstance(Class<T> beanClass, String beanName) {
		throw new UnsupportedOperationException();
	}

}
