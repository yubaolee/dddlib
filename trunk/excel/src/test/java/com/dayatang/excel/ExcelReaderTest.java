package com.dayatang.excel;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;

import org.junit.Before;
import org.junit.Test;

public class ExcelReaderTest {

	private ExcelReader importer;
	
	@Before
	public void setUp() throws Exception {
		importer = ExcelReader.fromClasspath("/import.xls");
	}

	@Test
	public void testExcelImporterFile() throws BiffException, IOException {
		assertEquals("Company", importer.getSheetName(0));
	}

	@Test
	public void testGetDataIntIntIntInt() throws Exception {
		List<Object[]> data = importer.read(0, 1, 0, 6);
		assertFalse(data.isEmpty());
		Object[] firstRow = data.get(0);
		assertTrue(firstRow.length > 0);
		assertEquals("suilink", firstRow[0]);
	}

	@Test
	public void testGetDataStringIntIntInt() throws Exception {
		List<Object[]> data = importer.read("Company", 1, 0, 6);
		assertFalse(data.isEmpty());
		Object[] firstRow = data.get(0);
		assertTrue(firstRow.length > 0);
		assertEquals("suilink", firstRow[0]);
	}
}
