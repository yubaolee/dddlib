package com.dayatang.provider;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.domain.InstanceProvider;
import com.dayatang.service.MyService2;
import com.dayatang.service.Service;

public class Jdk6InstanceProviderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		InstanceProvider provider = new Jdk6InstanceProvider();
		Service service = provider.getInstance(Service.class);
		//断言service的类型是MyService2或它的子类。
		assertTrue(MyService2.class.isAssignableFrom(service.getClass()));
	}

}
