package com.dayatang.excel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;


public class ExcelReaderTest {

	private ExcelReader importer;
	private File excelFile;
	
	@Before
	public void setUp() throws Exception {
		String excelFileName = getClass().getResource("/import.xls").toURI().toURL().getFile();
		excelFile = new File(excelFileName);
		importer = new ExcelReader(excelFile);
	}


	@Test
	public void testReadColumnIndexRange() throws Exception {
		ExcelRange range = new ExcelRange().rowFrom(1).columnRange(0, 6);
		ExcelRangeData data = importer.read(0, range);
		assertEquals(3, data.getRowCount());
		
		assertEquals("suilink", data.getString(0, 0));
		assertEquals("广州穗灵通讯科技有限公司", data.getString(0, 1));
		assertTrue(DateUtils.isSameDay(data.getDate(0, 2), parseDate(2002, 7, 1)));
		assertNull(data.getDate(0, 3));
		assertEquals(1, data.getInt(0, 4).intValue());
		assertEquals(1L, data.getLong(0, 4).longValue());
		assertEquals(1.0, data.getDouble(0, 4).doubleValue(), 0.0001);
		assertNull(data.getDate(0, 5));
		assertFalse(data.getBoolean(0, 6));
		assertTrue(data.getBoolean(1, 6));
		assertNull(data.getDate(2, 6));
	}

	@Test
	public void testReadColumnNameRange() throws Exception {
		ExcelRange range = new ExcelRange().rowFrom(1).columnRange("A", "G");
		ExcelRangeData data = importer.read(0, range);
		assertEquals(3, data.getRowCount());
		assertEquals("suilink", data.getString(0, 0));
		assertEquals("广州穗灵通讯科技有限公司", data.getString(0, 1));
		assertTrue(DateUtils.isSameDay(data.getDate(0, 2), parseDate(2002, 7, 1)));
		assertNull(data.getDate(0, 3));
		assertEquals(1, data.getInt(0, 4).intValue());
		assertEquals(1L, data.getLong(0, 4).longValue());
		assertEquals(1.0, data.getDouble(0, 4).doubleValue(), 0.0001);
		assertNull(data.getDate(0, 5));
		assertFalse(data.getBoolean(0, 6));
		assertTrue(data.getBoolean(1, 6));
		assertNull(data.getDate(2, 6));
	}
	
	@Test
	public void testReadFixedRows() throws Exception {
		ExcelRange range = new ExcelRange().rowFrom(1).rowTo(2).columnRange("A", "G");
		ExcelRangeData data = importer.read(0, range);
		assertEquals(2, data.getRowCount());
		
		assertEquals("suilink", data.getString(0, 0));
		assertEquals("广州穗灵通讯科技有限公司", data.getString(0, 1));
		assertTrue(DateUtils.isSameDay(data.getDate(0, 2), parseDate(2002, 7, 1)));
		assertNull(data.getDate(0, 3));
		assertEquals(1, data.getInt(0, 4).intValue());
		assertEquals(1L, data.getLong(0, 4).longValue());
		assertEquals(1.0, data.getDouble(0, 4).doubleValue(), 0.0001);
		assertNull(data.getDate(0, 5));
		assertFalse(data.getBoolean(0, 6));
		assertTrue(data.getBoolean(1, 6));
	}

	private Date parseDate(int year, int month, int date) {
		Calendar result = Calendar.getInstance();
		result.set(year, month - 1, date);
		return result.getTime();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testWrongNumeric() {
		ExcelRange range = new ExcelRange().rowFrom(1).rowTo(2).columnRange("A", "G");
		ExcelRangeData data = importer.read(0, range);
		data.getDouble(0, 1);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testWrongBoolean() {
		ExcelRange range = new ExcelRange().rowFrom(1).rowTo(2).columnRange("A", "G");
		ExcelRangeData data = importer.read(0, range);
		data.getBoolean(0, 1);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testWrongDate() {
		ExcelRange range = new ExcelRange().rowFrom(1).rowTo(2).columnRange("A", "G");
		ExcelRangeData data = importer.read(0, range);
		data.getDate(0, 1);
	}
}
