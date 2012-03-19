package com.dayatang.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel模板类。用于执行打开和关闭工作簿等公共行为。
 * @author yyang
 *
 */
public class ExcelReaderTemplate {
	private Workbook workbook;

	public ExcelReaderTemplate(File excelFile) throws FileNotFoundException, IOException {
		String extensionName = excelFile.getName();
		if (extensionName.endsWith(".xlsx") || extensionName.endsWith(".XLSX")) {
			workbook = new XSSFWorkbook(new FileInputStream(excelFile));
		} else {
			workbook = new HSSFWorkbook(new FileInputStream(excelFile));
		}
	}

	public ExcelReaderTemplate(InputStream in, Class<? extends Workbook> docType) throws IOException {
		if (docType == XSSFWorkbook.class) {
			workbook = new XSSFWorkbook(in);
		} else {
			workbook = new HSSFWorkbook(in);
		}
	}

	public <T> T execute(ExcelReaderCallback<T> callback) throws Exception {
		return callback.doInPoi(workbook);
	}
}
