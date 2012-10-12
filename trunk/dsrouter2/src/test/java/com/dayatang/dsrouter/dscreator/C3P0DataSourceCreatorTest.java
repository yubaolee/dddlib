package com.dayatang.dsrouter.dscreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.utils.Configuration;

public class C3P0DataSourceCreatorTest {

	private AbstractDataSourceCreator instance;
	private JdbcUrlTranslator urlTranslator; 
	private Configuration configuration;
	private DataSource dataSource;
	
	@Before
	public void setUp() throws Exception {
		urlTranslator = mock(JdbcUrlTranslator.class);
		configuration = mock(Configuration.class);
		instance = new C3P0DataSourceCreator(urlTranslator, configuration);
		dataSource = mock(DataSource.class);
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void createDataSourceForTenant() {
		String tenant = "xyz";
	}

}
