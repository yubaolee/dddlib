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
		String fileName = excelFile.getName().toLowerCase();
		if (fileName.endsWith(".xlsx")) {
			return new XSSFWorkbook(new FileInputStream(excelFile));
		} else {
			return new HSSFWorkbook(new FileInputStream(excelFile));
		}
	}
	
	public static Workbook createWorkbook(InputStream in, Class<? extends Workbook> docType) throws IOException {
		if (docType == XSSFWorkbook.class) {
			return new XSSFWorkbook(in);
		} else {
			return new HSSFWorkbook(in);
		}
	}
}
