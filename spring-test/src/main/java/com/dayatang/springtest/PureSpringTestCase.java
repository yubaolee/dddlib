package com.dayatang.springtest;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.spring.factory.SpringProvider;

public abstract class PureSpringTestCase {

	protected ApplicationContext context;

	// protected String[] springXmlPath() {
	// return new String[] { "classpath*:spring/*.xml" };
	// }

	protected abstract String[] springXmlPath();

	@Before
	public void setup() {
		InstanceFactory
				.setInstanceProvider(new SpringProvider(springXmlPath()));
		context = new ClassPathXmlApplicationContext(springXmlPath());
	}

	@After
	public void teardown() {
	}
}
