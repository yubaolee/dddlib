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
	public void testGetDataIntIntIntInt() throws Exception {
		ReadingRange range = new ReadingRange.Builder(0, 1).colRange(0, 5)
				.colTypes(DataType.STRING, DataType.STRING, DataType.DATE, DataType.DATE, DataType.NUMERIC, DataType.STRING).build();
		List<Object[]> data = importer.read(range);
		System.out.println(data.size());
		Object[] firstRow = data.get(0);
		assertTrue(firstRow.length > 0);
		assertEquals("suilink", firstRow[0]);
		Object[] lastRow = data.get(2);
		assertTrue(DateUtils.isSameDay((Date)lastRow[2], parseDate(2007, 7, 1)));
	}

	private Date parseDate(int year, int month, int date) {
		Calendar result = Calendar.getInstance();
		result.set(year, month - 1, date);
		return result.getTime();
	}

	@Test
	public void testGetDataStringIntIntInt() throws Exception {
		ReadingRange range = new ReadingRange.Builder("Company", 1).colRange(0, 5)
				.colTypes(DataType.STRING, DataType.STRING, DataType.DATE, DataType.DATE, DataType.NUMERIC, DataType.STRING).build();
		List<Object[]> data = importer.read(range);
		assertFalse(data.isEmpty());
		Object[] firstRow = data.get(0);
		assertTrue(firstRow.length > 0);
		assertEquals("suilink", firstRow[0]);
	}
}
