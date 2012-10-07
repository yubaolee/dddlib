package com.dayatang.dsrouter.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TenantHolderThreadLocalImplTest {
	
	private TenantHolderThreadLocalImpl instance;

	@Before
	public void setUp() throws Exception {
		instance = new TenantHolderThreadLocalImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertNull(instance.getTenantId());
		String tenantId = "abc";
		instance.setTenantId(tenantId);
		assertEquals(tenantId, instance.getTenantId());
		instance.removeTenantId();
		assertNull(instance.getTenantId());
	}

}
