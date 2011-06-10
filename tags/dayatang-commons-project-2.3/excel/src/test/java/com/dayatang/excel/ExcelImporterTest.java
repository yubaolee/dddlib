package com.dayatang.excel;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ExcelImporterTest {

	private ExcelImporter importer;
	
	@Before
	public void setUp() throws Exception {
		Resource resource = new ClassPathResource("import.xls");
		importer = new ExcelImporter(resource.getFile());
	}

	@After
	public void tearDown() throws Exception {
		importer.close();
	}

	@Test
	public void testExcelImporterFile() throws BiffException, IOException {
		Resource resource = new ClassPathResource("import.xls");
		ExcelImporter importer = new ExcelImporter(resource.getFile());
		assertEquals("Company", importer.getWorkbook().getSheet(0).getName());
		importer.close();
	}

	@Test
	public void testExcelImporterInputStream() throws BiffException, IOException {
		Resource resource = new ClassPathResource("import.xls");
		ExcelImporter importer = new ExcelImporter(resource.getInputStream());
		assertEquals("Company", importer.getWorkbook().getSheet(0).getName());
		importer.close();
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
