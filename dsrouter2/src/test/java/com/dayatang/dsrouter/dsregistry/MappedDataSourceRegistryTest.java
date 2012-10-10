package com.dayatang.dsrouter.dsregistry;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.dsrouter.dsregistry.MappedDataSourceRegistry;

public class MappedDataSourceRegistryTest {
	
	private MappedDataSourceRegistry instance;
	private DataSourceCreator dataSourceCreator;
	private DataSource dataSource;

	@Before
	public void setUp() throws Exception {
		String tenant = "abc";
		instance = new MappedDataSourceRegistry();
		dataSourceCreator = mock(DataSourceCreator.class);
		instance.setDataSourceCreator(dataSourceCreator);
		dataSource = mock(DataSource.class);
		when(dataSourceCreator.createDataSource(tenant)).thenReturn(dataSource);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOrCreateDataSourceByTenant() {
		String tenant = "abc";
		assertSame(dataSource, instance.getOrCreateDataSourceByTenant(tenant));
		verify(dataSourceCreator).createDataSource(tenant);
		reset(dataSourceCreator);
		assertSame(dataSource, instance.getOrCreateDataSourceByTenant(tenant));
		verify(dataSourceCreator, never()).createDataSource(tenant);
		assertEquals(1, instance.size());
		instance.releaseAllDataSources();
		assertEquals(0, instance.size());
	}
	
	@Test
	public void testReleaseDataSourceByTenant() {
		String tenant = "abc";
		assertSame(dataSource, instance.getOrCreateDataSourceByTenant(tenant));
		assertTrue(instance.exists(tenant));
		instance.releaseDataSourceByTenant(tenant);
		assertFalse(instance.exists(tenant));
	}
	
	@Test
	public void testLastAccess() throws InterruptedException {
		String tenant = "abc";
		assertNull(instance.getLastAccessTimeOfTenant(tenant));
		assertSame(dataSource, instance.getOrCreateDataSourceByTenant(tenant));
		Date lastAccess = instance.getLastAccessTimeOfTenant(tenant);
		assertTrue(System.currentTimeMillis() - lastAccess.getTime() < 100);
		TimeUnit.SECONDS.sleep(3);
		assertTrue(System.currentTimeMillis() - lastAccess.getTime() > 3000);
	}
	
	@Test
	public void testRegisterTenantDataSource() {
		String tenant = "abc";
		assertFalse(instance.exists(tenant));
		instance.registerTenantDataSource(tenant, dataSource);
		assertTrue(instance.exists(tenant));
		assertSame(dataSource, instance.getOrCreateDataSourceByTenant(tenant));
		verify(dataSourceCreator, never()).createDataSource(tenant);
	}
}
