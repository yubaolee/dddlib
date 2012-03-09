package com.dayatang.excel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;

/**
 * Excel模板类。用于执行打开和关闭工作簿等公共行为。
 * @author yyang
 *
 */
public class ExcelWriterTemplate {

	//代表Excel文件内容的输入流
	private OutputStream out;
	
	//执行Excel操作后是否需要关闭fileStream
	private boolean needCloseStream = false;

	private ExcelWriterTemplate(File excelFile) throws BiffException, IOException {
		out = new BufferedOutputStream(new FileOutputStream(excelFile));
	}

	private ExcelWriterTemplate(OutputStream out) throws BiffException, IOException {
		this.out = new BufferedOutputStream(out);
		
	}
	
	public static ExcelWriterTemplate fromOutputStream(OutputStream out) throws BiffException, IOException {
		return new ExcelWriterTemplate(out);
	}

	
	public static ExcelWriterTemplate fromFileSystem(String pathname) throws BiffException, IOException {
		return fromFile(new File(pathname));
	}
	
	public static ExcelWriterTemplate fromFile(File file) throws BiffException, IOException {
		if (!file.exists()) {
			throw new FileNotFoundException("File '" + file + "' not found!");
		}
		ExcelWriterTemplate result =  new ExcelWriterTemplate(file);
		result.needCloseStream = true;
		return result;
	}

	public void execute(ExcelWriterCallback callback) throws Exception {
		WritableWorkbook workbook = Workbook.createWorkbook(out);
		callback.doInJxl(workbook);
		workbook.write();
		workbook.close();
		if (needCloseStream) {
			out.close();
		}
	}
}
