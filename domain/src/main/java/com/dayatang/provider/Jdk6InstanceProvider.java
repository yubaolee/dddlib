package com.dayatang.provider;

import java.util.ServiceLoader;

import com.dayatang.domain.InstanceProvider;

/**
 * 用JDK6 ServiceLoader实现的实例提供者。使用它的话，根本不需要任何IoC框架，也不需要配置文件
 * 指明接口的实现类是哪个，系统会自行查找接口的实现类。
 * @author yyang
 *
 */
public class Jdk6InstanceProvider implements InstanceProvider {

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
