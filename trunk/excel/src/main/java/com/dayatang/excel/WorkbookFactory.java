package com.dayatang.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkbookFactory {
	public static Workbook createWorkbook(File excelFile) throws FileNotFoundException, IOException {
		return createWorkbook(new FileInputStream(excelFile), Version.as(excelFile.getName()));
	}
	
	public static Workbook createWorkbook(InputStream in, Version version) throws IOException {
		if (version == Version.XLSX) {
			return new XSSFWorkbook(in);
		} else {
			return new HSSFWorkbook(in);
		}
	}
}
