package com.dayatang.domain;

/**
 * 实例提供者接口。该接口封装了IoC工厂的具体实现。它抽象
 * 出IoC工厂的基本能力：提供某种指定类型（接口/类）的一个
 * 实例。
 * @author yyang
 *
 */
public interface InstanceProvider {

	/**
	 * 返回指定类型的实例。
	 * @param <T> 类型参数
	 * @param beanClass 实例的类型
	 * @return 指定类型的实例。
	 */
	<T> T getInstance(Class<T> beanClass);

	/**
	 * 返回指定类型的实例。
	 * @param <T> 类型参数
	 * @param beanName 实现类在容器中配置的名字
	 * @param beanClass 实例的类型
	 * @return 指定类型的实例。
	 */
	<T> T getInstance(Class<T> beanClass, String beanName);
}
