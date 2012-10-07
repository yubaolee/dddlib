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
		String tenantId = "abc";
		instance = new MappedDataSourceRegistry();
		dataSourceCreator = mock(DataSourceCreator.class);
		instance.setDataSourceCreator(dataSourceCreator);
		dataSource = mock(DataSource.class);
		when(dataSourceCreator.createDataSource(tenantId)).thenReturn(dataSource);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOrCreateDataSourceByTenantId() {
		String tenantId = "abc";
		assertSame(dataSource, instance.getOrCreateDataSourceByTenantId(tenantId));
		verify(dataSourceCreator).createDataSource(tenantId);
		reset(dataSourceCreator);
		assertSame(dataSource, instance.getOrCreateDataSourceByTenantId(tenantId));
		verify(dataSourceCreator, never()).createDataSource(tenantId);
		assertEquals(1, instance.size());
		instance.releaseAllDataSources();
		assertEquals(0, instance.size());
	}
	
	@Test
	public void testReleaseDataSourceByTenantId() {
		String tenantId = "abc";
		assertSame(dataSource, instance.getOrCreateDataSourceByTenantId(tenantId));
		assertTrue(instance.exists(tenantId));
		instance.releaseDataSourceByTenantId(tenantId);
		assertFalse(instance.exists(tenantId));
	}
	
	@Test
	public void testLastAccess() throws InterruptedException {
		String tenantId = "abc";
		assertNull(instance.getLastAccessTimeOfTenant(tenantId));
		assertSame(dataSource, instance.getOrCreateDataSourceByTenantId(tenantId));
		Date lastAccess = instance.getLastAccessTimeOfTenant(tenantId);
		assertTrue(System.currentTimeMillis() - lastAccess.getTime() < 100);
		TimeUnit.SECONDS.sleep(3);
		assertTrue(System.currentTimeMillis() - lastAccess.getTime() > 3000);
	}
}
