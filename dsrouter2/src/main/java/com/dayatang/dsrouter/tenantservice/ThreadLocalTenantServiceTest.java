package com.dayatang.dsrouter.tenantservice;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.tenantservice.ThreadLocalTenantHolder;

public class ThreadLocalTenantServiceTest {
	
	private ThreadLocalTenantService instance;

	@Before
	public void setUp() throws Exception {
		instance = new ThreadLocalTenantService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertNull(instance.getTenant());
		String tenant = "abc";
		ThreadLocalTenantHolder.setTenant(tenant);
		assertEquals(tenant, instance.getTenant());
		ThreadLocalTenantHolder.removeTenant();
		assertNull(instance.getTenant());
	}

}
