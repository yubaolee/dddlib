package com.dayatang.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

	private Class<? extends Workbook> docType = HSSFWorkbook.class;
	
	//代表Excel文件内容的输入流
	private InputStream in;
	
	//执行Excel操作后是否需要关闭in
	private boolean needCloseStream = false;

	public ExcelReaderTemplate(File excelFile) {
		String extensionName = excelFile.getName();
		if (extensionName.endsWith(".xlsx") || extensionName.endsWith(".XLSX")) {
			docType = XSSFWorkbook.class;
		} else {
			docType = HSSFWorkbook.class;
		}
		try {
			in = new BufferedInputStream(new FileInputStream(excelFile));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File '" + excelFile.getAbsolutePath() + "' not found.");
		}
	}

	public ExcelReaderTemplate(InputStream in, Class<? extends Workbook> docType) {
		this.in = new BufferedInputStream(in);
		this.docType = docType;
	}

	public <T> T execute(ExcelReaderCallback<T> callback) throws Exception {
		Workbook workbook;
		if (docType == XSSFWorkbook.class) {
			workbook = new XSSFWorkbook(in);
		} else {
			workbook = new HSSFWorkbook(in);
		}
		T result = callback.doInPoi(workbook);
		if (needCloseStream) {
			in.close();
		}
		return result;
	}
}
