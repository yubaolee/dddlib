package com.dayatang.excel;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExcelImporterTest {

	private ExcelImporter importer;
	
	@Before
	public void setUp() throws Exception {
		importer = new ExcelImporter(getClass().getResourceAsStream("/import.xls"));
	}

	@After
	public void tearDown() throws Exception {
		if (importer == null) {
			return;
		}
		importer.close();
	}

	@Test
	public void testExcelImporterFile() throws BiffException, IOException {
		assertEquals("Company", importer.getWorkbook().getSheet(0).getName());
	}

	@Test
	public void testExcelImporterInputStream() throws BiffException, IOException {
		assertEquals("Company", importer.getWorkbook().getSheet(0).getName());
	}

	@Test
	public void testGetDataIntIntIntInt() throws Exception {
		List<Object[]> data = importer.getData(0, 1, 0, 6);
		assertFalse(data.isEmpty());
		Object[] firstRow = data.get(0);
		assertTrue(firstRow.length > 0);
		assertEquals("suilink", firstRow[0]);
	}

	@Test
	public void testGetDataStringIntIntInt() throws Exception {
		List<Object[]> data = importer.getData("Company", 1, 0, 6);
		assertFalse(data.isEmpty());
		Object[] firstRow = data.get(0);
		assertTrue(firstRow.length > 0);
		assertEquals("suilink", firstRow[0]);
	}

}
