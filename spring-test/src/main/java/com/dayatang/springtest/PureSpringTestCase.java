package com.dayatang.springtest;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.spring.factory.SpringProvider;

public abstract class PureSpringTestCase {

	private static final Logger logger = LoggerFactory
			.getLogger(PureSpringTestCase.class);

	protected static ApplicationContext context;

	protected String[] springXmlPath() {
		return new String[] { "classpath:spring/*.xml" };
	}

	@Before
	public void setup() {
		if (context == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("初始化Spring上下文。");
			}
			InstanceFactory.setInstanceProvider(new SpringProvider(
					springXmlPath()));
			context = new ClassPathXmlApplicationContext(springXmlPath());
		}
	}

	@After
	public void teardown() {
	}
}
