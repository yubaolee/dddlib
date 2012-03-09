package com.dayatang.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Excel模板类。用于执行打开和关闭工作簿等公共行为。
 * @author yyang
 *
 */
public class ExcelReaderTemplate {

	//代表Excel文件内容的输入流
	private InputStream in;
	
	//执行Excel操作后是否需要关闭in
	private boolean needCloseStream = false;

	private ExcelReaderTemplate(File excelFile) throws IOException {
		in = new BufferedInputStream(new FileInputStream(excelFile));
	}

	private ExcelReaderTemplate(InputStream in) throws IOException {
		this.in = new BufferedInputStream(in);
		
	}
	
	public static ExcelReaderTemplate fromInputStream(InputStream in) throws IOException {
		return new ExcelReaderTemplate(in);
	}
	
	public static ExcelReaderTemplate fromClasspath(String pathname) throws IOException {
		ExcelReaderTemplate result = fromInputStream(ExcelReaderTemplate.class.getResourceAsStream(pathname));
		result.needCloseStream = true;
		return result;
	}
	
	public static ExcelReaderTemplate fromFileSystem(String pathname) throws IOException {
		return fromFile(new File(pathname));
	}
	
	public static ExcelReaderTemplate fromFile(File file) throws IOException {
		if (!file.exists()) {
			throw new FileNotFoundException("File '" + file + "' not found!");
		}
		ExcelReaderTemplate result =  new ExcelReaderTemplate(file);
		result.needCloseStream = true;
		return result;
	}

	public <T> T execute(ExcelReaderCallback<T> callback) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook(in);
		T result = callback.doInPoi(workbook);
		if (needCloseStream) {
			in.close();
		}
		return result;
	}
}
