package com.dayatang.dsrouter.dbmapper;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.Constants;
import com.dayatang.utils.Configuration;
import com.dayatang.utils.ConfigurationFileImpl;

public class PropertiesFileDbMapperTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void defaultCongiuration() {
		PropertiesFileDbMapper instance = new PropertiesFileDbMapper();
		assertEquals("db_abc", instance.getMappingValue("abc"));
		assertEquals("db_xyz", instance.getMappingValue("xyz"));
	}

	@Test
	public void testGetMappingValue() {
		Configuration configuration = ConfigurationFileImpl.fromClasspath(Constants.DB_MAPPING_FILE);
		PropertiesFileDbMapper instance = new PropertiesFileDbMapper(configuration);
		assertEquals("db_abc", instance.getMappingValue("abc"));
		assertEquals("db_xyz", instance.getMappingValue("xyz"));
	}

}
