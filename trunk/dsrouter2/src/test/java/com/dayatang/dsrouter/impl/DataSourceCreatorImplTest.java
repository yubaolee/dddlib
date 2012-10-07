package com.dayatang.dsrouter.impl;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataSourceCreatorImplTest {

	private DataSourceCreatorImpl instance;
	
	@Before
	public void setUp() throws Exception {
		instance = new DataSourceCreatorImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateDataSource() {
		String tenantId = "abc";
		DataSource result = instance.createDataSource(tenantId);
		assertEquals(DbType.MYSQL, instance.getDbType());
		assertEquals(PoolType.C3P0, instance.getPoolType());
	}

}
