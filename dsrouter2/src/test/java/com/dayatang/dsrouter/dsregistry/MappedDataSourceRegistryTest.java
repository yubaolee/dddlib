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
	private String tenant = "abc";

	@Before
	public void setUp() throws Exception {
		instance = new MappedDataSourceRegistry();
		dataSourceCreator = mock(DataSourceCreator.class);
		instance.setDataSourceCreator(dataSourceCreator);
		dataSource = mock(DataSource.class);
		when(dataSourceCreator.createDataSource(tenant)).thenReturn(dataSource);
		instance.releaseAllDataSources();
	}

	@After
	public void tearDown() throws Exception {
		instance.releaseAllDataSources();
	}

	@Test
	public void getDataSourceOfTenant() {
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		verify(dataSourceCreator).createDataSource(tenant);
		reset(dataSourceCreator);
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		verify(dataSourceCreator, never()).createDataSource(tenant);
		assertEquals(1, instance.size());
		instance.releaseAllDataSources();
		assertEquals(0, instance.size());
	}
	
	@Test
	public void registerDataSourceForTenant() {
		assertFalse(instance.exists(tenant));
		instance.registerDataSourceForTenant(tenant, dataSource);
		assertTrue(instance.exists(tenant));
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		verify(dataSourceCreator, never()).createDataSource(tenant);
	}
	
	@Test
	public void releaseDataSourceOfTenant() {
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		assertTrue(instance.exists(tenant));
		instance.releaseDataSourceOfTenant(tenant);
		assertFalse(instance.exists(tenant));
	}
	
	@Test
	public void releaseAllDataSources() {
		instance.registerDataSourceForTenant("abc", mock(DataSource.class));
		instance.registerDataSourceForTenant("xyz", mock(DataSource.class));
		assertEquals(2, instance.size());
		instance.releaseAllDataSources();
		assertTrue(instance.size() == 0);
	}
	
	@Test
	public void getLastAccessTimeOfTenant() throws InterruptedException {
		assertNull(instance.getLastAccessTimeOfTenant(tenant));
		assertSame(dataSource, instance.getDataSourceOfTenant(tenant));
		Date lastAccess = instance.getLastAccessTimeOfTenant(tenant);
		assertTrue(System.currentTimeMillis() - lastAccess.getTime() < 100);
		TimeUnit.SECONDS.sleep(1);
		assertTrue(System.currentTimeMillis() - lastAccess.getTime() > 1000);
	}
}
