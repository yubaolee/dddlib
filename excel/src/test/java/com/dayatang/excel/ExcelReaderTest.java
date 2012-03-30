package com.dayatang.excel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

public class ExcelReaderTest {

	private ExcelReader importer;
	
	@Before
	public void setUp() throws Exception {
		String excelFile = getClass().getResource("/import.xls").toURI().toURL().getFile();
		importer = new ExcelReader(new File(excelFile));
	}

	@Test
	public void testReadCellValueWithSheetIndex() throws Exception {
		Date result = (Date) importer.readCellValue(0, 2, 3, DataType.DATE);
		assertTrue(DateUtils.isSameDay(result, parseDate(8888, 1, 1)));
	}

	@Test
	public void testReadCellValueWithSheetName() throws Exception {
		Date result = (Date) importer.readCellValue("Company", 2, 3, DataType.DATE);
		assertTrue(DateUtils.isSameDay(result, parseDate(8888, 1, 1)));
	}
	
	
	@Test
	public void testReadColRange() throws Exception {
		ReadingRange range = new ReadingRange.Builder().sheetAt(0).rowFrom(1).colRange(0, 5)
				.colTypes(DataType.STRING, DataType.STRING, DataType.DATE, DataType.DATE, DataType.NUMERIC, DataType.STRING).build();
		List<Object[]> data = importer.read(range);
		assertEquals(3, data.size());
		assertEquals("suilink", getData(data, 0, 0, String.class));
		assertTrue(DateUtils.isSameDay(getData(data, 2, 2, Date.class), parseDate(2007, 7, 1)));
	}
	
	@Test
	public void testReadColumnNameRange() throws Exception {
		ReadingRange range = new ReadingRange.Builder().sheetAt(0).rowFrom(1).colRange("A", "F")
				.colTypes(DataType.STRING, DataType.STRING, DataType.DATE, DataType.DATE, DataType.NUMERIC, DataType.STRING).build();
		List<Object[]> data = importer.read(range);
		assertEquals(3, data.size());
		assertEquals("suilink", getData(data, 0, 0, String.class));
		assertTrue(DateUtils.isSameDay(getData(data, 2, 2, Date.class), parseDate(2007, 7, 1)));
	}

	@Test
	public void testReadColumns() throws Exception {
		ReadingRange range = new ReadingRange.Builder().sheetAt(0).rowFrom(1).columns(0, 3)
				.colTypes(DataType.STRING, DataType.DATE).build();
		List<Object[]> data = importer.read(range);
		assertEquals(3, data.size());
		assertEquals("suilink", getData(data, 0, 0, String.class));
		assertTrue(DateUtils.isSameDay(getData(data, 2, 1, Date.class), parseDate(8888, 1, 1)));
	}

	@Test
	public void testReadColumnNamess() throws Exception {
		ReadingRange range = new ReadingRange.Builder().sheetAt(0).rowFrom(1).columns("A", "D")
				.colTypes(DataType.STRING, DataType.DATE).build();
		List<Object[]> data = importer.read(range);
		assertEquals(3, data.size());
		assertEquals("suilink", getData(data, 0, 0, String.class));
		assertTrue(DateUtils.isSameDay(getData(data, 2, 1, Date.class), parseDate(8888, 1, 1)));
	}
	
	@Test
	public void testReadFixedRows() throws Exception {
		ReadingRange range = new ReadingRange.Builder().sheetAt(0).rowFrom(1).rowTo(2).colRange(0, 5)
				.colTypes(DataType.STRING, DataType.STRING, DataType.DATE, DataType.DATE, DataType.NUMERIC, DataType.STRING).build();
		assertEquals(2, importer.read(range).size());
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getData(List<Object[]> data, int row, int column, Class<T> clazz) {
		return (T) data.get(row)[column];
	}

	private Date parseDate(int year, int month, int date) {
		Calendar result = Calendar.getInstance();
		result.set(year, month - 1, date);
		return result.getTime();
	}
}
