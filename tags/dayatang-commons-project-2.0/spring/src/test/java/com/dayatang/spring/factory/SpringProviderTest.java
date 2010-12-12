package com.dayatang.spring.factory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.spring.factory.SpringProvider;

public class SpringProviderTest {
	
	private SpringProvider springProvider;
	
	@Test
	public void testConstructorFromXmlPath() {
		springProvider = new SpringProvider(new String[] {"classpath:applicationContext-single-impl.xml"});
		InstanceFactory.setInstanceProvider(springProvider);
		assertEquals("I am Sruvice 1", springProvider.getInstance(Service.class).sayHello());
	}
	
	@Test
	public void testConstructorFromApplicationContext() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"classpath:applicationContext-single-impl.xml"});
		springProvider = new SpringProvider(applicationContext);
		InstanceFactory.setInstanceProvider(springProvider);
		assertEquals("I am Sruvice 1", springProvider.getInstance(Service.class).sayHello());
	}

	@Test
	public void testGetInstanceClassOfT() {
		springProvider = new SpringProvider(new String[] {"classpath:applicationContext-single-impl.xml"});
		InstanceFactory.setInstanceProvider(springProvider);
		assertEquals("I am Sruvice 1", springProvider.getInstance(Service.class).sayHello());
	}

	@Test
	public void testGetInstanceStringClassOfT() {
		springProvider = new SpringProvider(new String[] {"classpath:applicationContext-multi-impl.xml"});
		InstanceFactory.setInstanceProvider(springProvider);
		assertEquals("I am Sruvice 1", springProvider.getInstance(Service.class, "service1").sayHello());
		assertEquals("I am Sruvice 2", springProvider.getInstance(Service.class, "service2").sayHello());
	}

}

