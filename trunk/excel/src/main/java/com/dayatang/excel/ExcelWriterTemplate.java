package com.dayatang.excel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

/**
 * Excel模板类。用于执行打开和关闭工作簿等公共行为。
 * @author yyang
 *
 */
public class ExcelWriterTemplate {

	//代表Excel文件内容的输入流
	private OutputStream out;
	private InputStream in;
	private boolean needCloseOut = true;
	private boolean needCloseIn = true;

	private ExcelWriterTemplate() {
	}

	public static ExcelWriterTemplate to(File file) {
		ExcelWriterTemplate result = new ExcelWriterTemplate();
		try {
			result.out = new BufferedOutputStream(new FileOutputStream(file));
			result.needCloseOut = true;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Output file not found!");
		}
		return result;
	}
	
	public static ExcelWriterTemplate to(OutputStream outputStream) {
		ExcelWriterTemplate result = new ExcelWriterTemplate();
		result.out = new BufferedOutputStream(outputStream);
		result.needCloseOut = false;
		return result;
	}
	
	public void setTemplate(File file) {
		System.out.println(file.getAbsolutePath());
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			needCloseIn = true;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Template file not found!");
		}
	}
	
	public void setTemplate(InputStream inputStream) {
		in = new BufferedInputStream(inputStream);
		needCloseIn = false;
	}
	
	public void execute(ExcelWriterCallback callback) throws Exception {
		WritableWorkbook workbook;
		Workbook template = null;
		if (in == null) {
			workbook = Workbook.createWorkbook(out);
		} else {
			//System.out.println(in.available());
			template = Workbook.getWorkbook(in);
			workbook = Workbook.createWorkbook(out, template);
			//workbook = Workbook.createWorkbook(out);
		}
		//callback.doInJxl(workbook);
		workbook.write();
		workbook.close();
		if (needCloseOut) {
			out.close();
		}
		if (in != null && needCloseIn) {
			in.close();
			template.close();
		}
	}
}
