package com.dayatang.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.utils.DateUtils;

public class ConfigurationFileImplTest {

	private ConfigurationFileImpl instance;
	
	@Before
	public void setUp() throws Exception {
		instance = ConfigurationFileImpl.fromClasspath("/conf.properties");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFromClasspath() {
		instance = ConfigurationFileImpl.fromClasspath("/conf.properties");
		assertTrue(instance.getProperties().size() > 0);
	}

	@Test
	public void testFromPathname() {
		String pathname = getClass().getResource("/conf.properties").getFile();
		instance = ConfigurationFileImpl.fromFileSystem(pathname);
		assertTrue(instance.getProperties().size() > 0);
	}

	@Test
	public void testFromDirAndFile() {
		String pathname = getClass().getResource("/conf.properties").getFile();
		File file = new File(pathname);
		String dir = file.getParent();
		String fileName = file.getName();
		instance = ConfigurationFileImpl.fromFileSystem(dir, fileName);
		assertTrue(instance.getProperties().size() > 0);
	}

	@Test
	public void testUsePrefix() {
		instance.usePrefix("org.dayatang");
		assertTrue(instance.getBoolean("finished"));
	}

	@Test
	public void testSetDatePattern() {
		instance.setDatePattern("yyyy-MM-dd HH:mm:ss");
		Calendar result = Calendar.getInstance();
		result.set(2011, 0, 15, 12, 0, 0);
		result.set(Calendar.MILLISECOND, 0);
		assertEquals(result.getTime(), instance.getDate("time"));
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
		instance.setDatePattern("yyyy-MM-dd");
		assertEquals(orig, instance.getDate("birthday", defaultDate));
		assertEquals(defaultDate, instance.getDate("birthday1", defaultDate));
	}

	@Test
	public void testGetDateString() {
		Date orig = DateUtils.parseDate("2002-05-11");
		instance.setDatePattern("yyyy-MM-dd");
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
		instance = ConfigurationFileImpl.fromClasspath("/conf.properties");
		assertEquals("yyyy-MM-dd", instance.getString("xyz"));
	}

	@Test
	public void testGetProperties() {
		Properties properties = instance.getProperties();
		assertEquals("15", properties.get("size"));
	}

}
