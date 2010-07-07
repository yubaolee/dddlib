package com.dayatang.springtest;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.spring.factory.SpringProvider;

public abstract class PureSpringTestCase {

	private static final Logger logger = LoggerFactory
			.getLogger(PureSpringTestCase.class);

	protected static boolean alreadyInit = false;

	protected String[] springXmlPath() {
		return new String[] { "classpath:spring/*.xml" };
	}

	@Before
	public void setup() {
		if (!alreadyInit) {
			if (logger.isDebugEnabled()) {
				logger.debug("初始化Spring上下文。");
			}
			InstanceFactory.setInstanceProvider(new SpringProvider(
					springXmlPath()));
			alreadyInit = true;
		}
	}

	@After
	public void teardown() {
	}
}
