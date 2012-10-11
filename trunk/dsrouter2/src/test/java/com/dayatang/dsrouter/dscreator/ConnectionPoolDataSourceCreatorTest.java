package com.dayatang.dsrouter.dscreator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.dayatang.utils.Configuration;

public class ConnectionPoolDataSourceCreatorTest {

	private ConnectionPoolDataSourceCreator instance;
	private Configuration configuration;
	private ConnectionPoolType connectionPoolType;
	private DataSource dataSource;
	private String tenant = "abc";
	
	@Before
	public void setUp() throws Exception {
		instance = new ConnectionPoolDataSourceCreator();
		configuration = mock(Configuration.class);
		connectionPoolType = mock(ConnectionPoolType.class);
		instance.setConfiguration(configuration);
		dataSource = mock(DataSource.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void createDataSourceForTenant() {
		String tenant = "xyz";
		DataSource result = instance.createDataSourceForTenant(tenant);
		assertNotNull(result);
		assertEquals(ConnectionPoolType.C3P0, instance.getPoolType());
	}

	@Test
	public void createDataSourceForTenantConnectionPoolTypeSetted() throws Exception {
		Properties properties = new Properties();
		when(configuration.getProperties()).thenReturn(properties);
		when(connectionPoolType.createDataSource(tenant, properties)).thenReturn(dataSource);
		instance.setPoolType(connectionPoolType);
		assertEquals(dataSource, instance.createDataSourceForTenant(tenant));
	}

	@Test
	public void createDataSourceForTenantConnectionPoolTypeNotSetted() {
		when(configuration.getString(Constants.CONNECTION_POOL_TYPE)).thenReturn("C3P0");
		assertEquals(ConnectionPoolType.C3P0, instance.getPoolType());
	}
}
