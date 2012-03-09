package com.dayatang.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Excel模板类。用于执行打开和关闭工作簿等公共行为。
 * @author yyang
 *
 */
public class ExcelTemplate {

	//代表Excel文件内容的输入流
	private InputStream fileStream;
	
	//执行Excel操作后是否需要关闭fileStream
	private boolean needCloseStream = false;

	private ExcelTemplate(File excelFile) throws BiffException, IOException {
		fileStream = new FileInputStream(excelFile);
	}

	private ExcelTemplate(InputStream fileStream) throws BiffException, IOException {
		this.fileStream = fileStream;
	}
	
	public static ExcelTemplate fromInputStream(InputStream in) throws BiffException, IOException {
		return new ExcelTemplate(in);
	}
	
	public static ExcelTemplate fromClasspath(String pathname) throws BiffException, IOException {
		ExcelTemplate result = fromInputStream(ExcelTemplate.class.getResourceAsStream(pathname));
		result.needCloseStream = true;
		return result;
	}
	
	public static ExcelTemplate fromFileSystem(String pathname) throws BiffException, IOException {
		return fromFile(new File(pathname));
	}
	
	public static ExcelTemplate fromFile(File file) throws BiffException, IOException {
		if (!file.exists()) {
			throw new FileNotFoundException("File '" + file + "' not found!");
		}
		ExcelTemplate result =  new ExcelTemplate(file);
		result.needCloseStream = true;
		return result;
	}

	public <T> T execute(ExcelCallback<T> callback) throws BiffException, IOException {
		Workbook workbook = Workbook.getWorkbook(fileStream);
		T result = callback.doInJxl(workbook);
		workbook.close();
		if (needCloseStream) {
			fileStream.close();
		}
		return result;
	}
}
