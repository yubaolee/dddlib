package com.dayatang.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConfigurationDbImplTest {
	
	private static DataSource dataSource;
	private static String TABLE_NAME = "SYS_CONFIG";
	private ConfigurationDbImpl instance;

	@BeforeClass
	public static void classSetUp() throws Exception {
		dataSource = createDataSource();
		//createDbSchemaIfNotExists();
	}

	private static DataSource createDataSource() {
		final ComboPooledDataSource result = new ComboPooledDataSource();
		try {
			result.setDriverClass("org.h2.Driver");
		} catch (PropertyVetoException e) {
			throw new RuntimeException("Cannot access JDBC Driver: org.h2.Driver", e);
		}
		result.setJdbcUrl("jdbc:h2:mem:testdb");
		result.setUser("sa");
		//result.setPassword(password);
		result.setMinPoolSize(10);
		result.setMaxPoolSize(300);
		result.setInitialPoolSize(10);
		result.setAcquireIncrement(5);
		result.setMaxStatements(0);
		result.setIdleConnectionTestPeriod(60);
		result.setAcquireRetryAttempts(30);
		result.setBreakAfterAcquireFailure(false);
		result.setTestConnectionOnCheckout(false);
		return result;
	}


	@Before
	public void setUp() throws Exception {
		readConfigFromFile();
		instance = new ConfigurationDbImpl(dataSource, TABLE_NAME);
	}

	private static void readConfigFromFile() throws IOException {
		Properties props = new Properties();
		props.load(new InputStreamReader(ConfigurationDbImplTest.class.getResourceAsStream("/conf.properties"), "UTF-8") );
		ConfigurationDbImpl dbImpl = new ConfigurationDbImpl(dataSource, TABLE_NAME);
		for (Object key : props.keySet()) {
			dbImpl.setString((String) key, props.getProperty((String) key));
		}
		//dbImpl.setString("name", "张三");
		dbImpl.save();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUsePrefix() {
		instance.usePrefix("org.dayatang");
		assertTrue(instance.getBoolean("finished"));
	}

	@Test
	public void testGetStringStringString() {
		assertEquals("yyyy-M-d", instance.getString("date.format", "yyyy-MM-dd"));
		assertEquals("yyyy-MM-dd", instance.getString("format", "yyyy-MM-dd"));
	}

	@Test
	public void testGetStringString() {
		assertEquals("yyyy-M-d", instance.getString("date.format"));
		assertEquals("", instance.getString("format"));
		assertEquals("张三", instance.getString("name"));
	}

	@Test
	public void testSetString() {
		instance.setString("date.format", "yyyy-MM-dd");
		assertEquals("yyyy-MM-dd", instance.getString("date.format"));
	}

	@Test
	public void testGetIntStringInt() {
		assertEquals(15, instance.getInt("size", 20));
		assertEquals(20, instance.getInt("size1", 20));
	}

	@Test
	public void testGetIntString() {
		assertEquals(15, instance.getInt("size"));
		assertEquals(0, instance.getInt("size1"));
	}

	@Test
	public void testSetInt() {
		instance.setInt("size", 150);
		assertEquals(150, instance.getInt("size"));
	}

	@Test
	public void testGetLongStringLong() {
		assertEquals(15L, instance.getLong("size", 20L));
		assertEquals(20L, instance.getLong("size1", 20L));
	}

	@Test
	public void testGetLongString() {
		assertEquals(15L, instance.getLong("size"));
		assertEquals(0L, instance.getLong("size1"));
	}

	@Test
	public void testSetLong() {
		instance.setLong("size", 150L);
		assertEquals(150L, instance.getLong("size"));
	}

	@Test
	public void testGetDoubleStringDouble() {
		assertEquals(15D, instance.getDouble("size", 20D), 0.001);
		assertEquals(20D, instance.getDouble("size1", 20D), 0.001);
	}

	@Test
	public void testGetDoubleString() {
		assertEquals(15D, instance.getDouble("size"), 0.001);
		assertEquals(0D, instance.getDouble("size1"), 0.001);
	}

	@Test
	public void testSetDouble() {
		instance.setDouble("size", 150D);
		assertEquals(150D, instance.getDouble("size"), 0.001);
	}

	@Test
	public void testGetBooleanStringBoolean() {
		assertTrue(instance.getBoolean("closed", false));
		assertTrue(instance.getBoolean("closed1", true));
	}

	@Test
	public void testGetBooleanString() {
		assertTrue(instance.getBoolean("closed"));
		assertFalse(instance.getBoolean("closed1"));
	}

	@Test
	public void testSetBoolean() {
		instance.setBoolean("closed", false);
		assertFalse(instance.getBoolean("size"));
	}

	@Test
	public void testGetDateStringDate() {
		Date orig = DateUtils.parseDate("2002-05-11");
		Date defaultDate = DateUtils.parseDate("2008-05-11");
		assertEquals(orig, instance.getDate("birthday", defaultDate));
		assertEquals(defaultDate, instance.getDate("birthday1", defaultDate));
	}

	@Test
	public void testGetDateString() {
		Date orig = DateUtils.parseDate("2002-05-11");
		assertEquals(orig, instance.getDate("birthday"));
		assertEquals(null, instance.getDate("birthday1"));
	}

	@Test
	public void testSetDate() {
		Date newDate = DateUtils.parseDate("2008-05-11");
		instance.setDate("birthday", newDate);
		assertEquals(newDate, instance.getDate("birthday"));
	}

	@Test
	public void testSave() {
		instance.setString("xyz", "yyyy-MM-dd");
		instance.save();
		ConfigurationDbImpl instance2 = new ConfigurationDbImpl(dataSource, TABLE_NAME);
		assertEquals("yyyy-MM-dd", instance2.getString("xyz"));
	}

	@Test
	public void testGetProperties() {
		Properties properties = instance.getProperties();
		assertEquals("15", properties.get("size"));
	}

}
