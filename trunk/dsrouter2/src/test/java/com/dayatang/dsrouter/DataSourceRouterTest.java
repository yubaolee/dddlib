package com.dayatang.dsrouter;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataSourceRouterTest {
	
	private DataSourceFactory instance;
	private DataSourceRouter router;

	@Before
	public void setUp() throws Exception {
		router = mock(DataSourceRouter.class);
		instance = new DataSourceFactory(router);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		DataSource dataSource = mock(DataSource.class);
		when(router.getOrCreateDataSource()).thenReturn(dataSource);
		assertSame(dataSource, instance.createDataSource());
	}

}
