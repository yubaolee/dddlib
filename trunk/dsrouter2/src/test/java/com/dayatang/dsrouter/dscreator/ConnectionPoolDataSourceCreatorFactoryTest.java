package com.dayatang.dsrouter.dscreator;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.utils.Configuration;

public class ConnectionPoolDataSourceCreatorFactoryTest {
	private ConnectionPoolDataSourceCreatorFactory instance;
	private Configuration configuration;

	@Before
	public void setUp() throws Exception {
		instance = new ConnectionPoolDataSourceCreatorFactory();
		configuration = mock(Configuration.class);
		instance.setConfiguration(configuration);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		when(configuration.getString(Constants.CONNECTION_POOL_TYPE)).thenReturn("C3P0");
		assertTrue(instance.getInstance() instanceof C3P0DataSourceCreator);
		when(configuration.getString(Constants.CONNECTION_POOL_TYPE)).thenReturn("PROXOOL");
		assertTrue(instance.getInstance() instanceof ProxoolDataSourceCreator);
		when(configuration.getString(Constants.CONNECTION_POOL_TYPE)).thenReturn("COMMONS_DBCP");
		assertTrue(instance.getInstance() instanceof CommonsDbcpDataSourceCreator);
	}

}
