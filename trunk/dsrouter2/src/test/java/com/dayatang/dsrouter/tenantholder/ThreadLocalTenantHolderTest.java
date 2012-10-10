package com.dayatang.dsrouter.tenantholder;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.tenantholder.ThreadLocalTenantHolder;

public class ThreadLocalTenantHolderTest {
	
	private ThreadLocalTenantHolder instance;

	@Before
	public void setUp() throws Exception {
		instance = new ThreadLocalTenantHolder();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertNull(instance.getTenant());
		String tenant = "abc";
		instance.setTenant(tenant);
		assertEquals(tenant, instance.getTenant());
		instance.removeTenant();
		assertNull(instance.getTenant());
	}

}
