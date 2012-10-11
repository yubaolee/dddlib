package com.dayatang.dsrouter.dscreator;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.dscreator.ConnectionPoolDataSourceCreator;
import com.dayatang.dsrouter.dscreator.ConnectionPoolType;
import com.dayatang.utils.Configuration;

public class ConnectionPoolDataSourceCreatorTest {

	private ConnectionPoolDataSourceCreator instance;
	private Configuration configuration;
	private ConnectionPoolType connectionPoolType;
	
	@Before
	public void setUp() throws Exception {
		instance = new ConnectionPoolDataSourceCreator();
		configuration = mock(Configuration.class);
		connectionPoolType = mock(ConnectionPoolType.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createDataSourceForTenant() {
		String tenant = "xyz";
		DataSource result = instance.createDataSourceForTenant(tenant);
		assertNotNull(result);
		assertEquals(ConnectionPoolType.C3P0, instance.getPoolType());
	}

}
