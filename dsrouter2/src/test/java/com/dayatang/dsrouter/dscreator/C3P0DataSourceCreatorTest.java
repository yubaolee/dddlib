package com.dayatang.dsrouter.dscreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.dayatang.utils.Configuration;
import com.dayatang.utils.ConfigurationFileImpl;

public class C3P0DataSourceCreatorTest {

	private AbstractDataSourceCreator instance;
	private JdbcUrlTranslator urlTranslator; 
	private Properties properties;
	private DataSource dataSource;
	
	@Before
	public void setUp() throws Exception {
		properties = mock(Properties.class);
		instance = new C3P0DataSourceCreator(properties);
		urlTranslator = mock(JdbcUrlTranslator.class);
		instance.setUrlTranslator(urlTranslator);
		dataSource = mock(DataSource.class);
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void createDataSourceForTenant() {
		String tenant = "xyz";
		when(properties.getProperty(Constants.JDBC_DRIVER_CLASS_NAME)).thenReturn("com.mysql.jdbc.Driver");
		when(properties.getProperty(Constants.JDBC_DRIVER_CLASS_NAME)).thenReturn("com.mysql.jdbc.Driver");
		when(urlTranslator.translateUrl(tenant, properties)).thenReturn("mysql://localhost:3306/abc");
		assertSame(dataSource, instance.createDataSourceForTenant(tenant));
	}

}
