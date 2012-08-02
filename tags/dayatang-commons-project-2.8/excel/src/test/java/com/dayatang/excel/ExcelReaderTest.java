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
	public void testReadColumns() throws Exception {
		int[] columnIndexes = new int[] {0, 3};
		DataType[] columnTypes = new DataType[] {DataType.STRING, DataType.DATE};
		ReadingRange range = new ReadingRange.Builder().sheetAt(0).rowFrom(1).columns(columnIndexes, columnTypes).build();
		List<Object[]> data = importer.read(range);
		assertEquals(3, data.size());
		assertEquals("suilink", getData(data, 0, 0, String.class));
		assertTrue(DateUtils.isSameDay(getData(data, 2, 1, Date.class), parseDate(8888, 1, 1)));
	}

	@Test
	public void testReadColumnNames() throws Exception {
		String[] columnLabels = new String[] {"A", "D"};
		DataType[] columnTypes = new DataType[] {DataType.STRING, DataType.DATE};
		ReadingRange range = new ReadingRange.Builder().sheetAt(0).rowFrom(1).columns(columnLabels, columnTypes).build();
		List<Object[]> data = importer.read(range);
		assertEquals(3, data.size());
		assertEquals("suilink", getData(data, 0, 0, String.class));
		assertTrue(DateUtils.isSameDay(getData(data, 2, 1, Date.class), parseDate(8888, 1, 1)));
	}
	
	@Test
	public void testReadFixedRows() throws Exception {
		String[] columnLabels = new String[] {"A", "D"};
		DataType[] columnTypes = new DataType[] {DataType.STRING, DataType.DATE};
		ReadingRange range = new ReadingRange.Builder().sheetAt(0).rowFrom(1).rowTo(2).columns(columnLabels, columnTypes).build();
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
	
	//@Test
	public void testComm() throws Exception {
		String excelFile = getClass().getResource("/REPORT0306.xls").toURI().toURL().getFile();
		importer = new ExcelReader(new File(excelFile));
		Double result = (Double) importer.readCellValue("REPORT", 2, 12, DataType.NUMERIC);
		assertEquals(0.00875, result.doubleValue(), 0.00001);
		int[] columnLabels = new int[] {12};
		DataType[] columnTypes = new DataType[] {DataType.NUMERIC};
		ReadingRange range = new ReadingRange.Builder().sheetAt(0).rowFrom(1).columns(columnLabels, columnTypes).build();
		List<Object[]> data = importer.read(range);
		for (int i = 280; i < 300; i++) {
			System.out.println((i + 2) + ": " + ((Double) data.get(i)[0]).doubleValue() * 100);
		}
	}
}
