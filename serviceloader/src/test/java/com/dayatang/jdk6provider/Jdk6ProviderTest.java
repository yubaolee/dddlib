package com.dayatang.jdk6provider;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.domain.InstanceProvider;

public class Jdk6ProviderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		InstanceProvider provider = new Jdk6Provider();
		Service service = provider.getInstance(Service.class);
		//断言service的类型是MyService2或它的子类。
		assertTrue(MyService2.class.isAssignableFrom(service.getClass()));
	}

}
