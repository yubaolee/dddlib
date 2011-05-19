package com.dayatang.spring.factory;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dayatang.commons.ioc.AbstractInstanceProviderTest;
import com.dayatang.commons.ioc.MyService1;
import com.dayatang.commons.ioc.Service;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.InstanceProvider;
import com.dayatang.spring.factory.SpringProvider;

public class SpringProviderTest extends AbstractInstanceProviderTest {
	
	private SpringProvider springProvider;

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new SpringProvider(new String[] {"classpath:applicationContext-multi-impl.xml"});
	}
	
	@Test
	public void testConstructorFromXmlPath() {
		springProvider = new SpringProvider(new String[] {"classpath:applicationContext-single-impl.xml"});
		InstanceFactory.setInstanceProvider(springProvider);
		Service service = springProvider.getInstance(Service.class);
		assertTrue(MyService1.class.isAssignableFrom(service.getClass()));
	}
	
	@Test
	public void testConstructorFromApplicationContext() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"classpath:applicationContext-single-impl.xml"});
		springProvider = new SpringProvider(applicationContext);
		InstanceFactory.setInstanceProvider(springProvider);
		Service service = springProvider.getInstance(Service.class);
		assertTrue(MyService1.class.isAssignableFrom(service.getClass()));
	}

}

