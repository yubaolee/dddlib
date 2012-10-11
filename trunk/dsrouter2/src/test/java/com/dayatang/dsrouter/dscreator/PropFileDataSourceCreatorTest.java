package com.dayatang.dsrouter.dscreator;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.dscreator.PropFileDataSourceCreator;
import com.dayatang.dsrouter.dscreator.PoolType;

public class PropFileDataSourceCreatorTest {

	private PropFileDataSourceCreator instance;
	
	@Before
	public void setUp() throws Exception {
		instance = new PropFileDataSourceCreator();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createDataSourceForTenant() {
		String tenant = "xyz";
		DataSource result = instance.createDataSourceForTenant(tenant);
		assertNotNull(result);
		assertEquals(PoolType.C3P0, instance.getPoolType());
	}

}
