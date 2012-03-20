package com.dayatang.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel模板类。用于执行打开和关闭工作簿等公共行为。
 * @author yyang
 *
 */
public class ExcelReaderTemplate {
	private Workbook workbook;

	public ExcelReaderTemplate(File excelFile) throws FileNotFoundException, IOException {
		workbook = WorkbookFactory.createWorkbook(excelFile);
	}

	public ExcelReaderTemplate(InputStream in, Version version) throws IOException {
		workbook = WorkbookFactory.createWorkbook(in, version);
	}

	public <T> T execute(ExcelReaderCallback<T> callback) throws Exception {
		return callback.doInPoi(workbook);
	}
}
