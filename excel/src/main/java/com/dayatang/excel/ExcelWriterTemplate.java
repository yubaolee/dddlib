package com.dayatang.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel模板类。用于执行打开和关闭工作簿等公共行为。
 * @author yyang
 *
 */
public class ExcelWriterTemplate {

	private File file;
	private OutputStream out;

	public ExcelWriterTemplate(File file) {
		this.file = file;
	}
	
	public ExcelWriterTemplate(OutputStream out) {
		super();
		this.out = out;
	}

	public ExcelWriterTemplate(File file, OutputStream out) {
		super();
		this.file = file;
		this.out = out;
	}
	
	public void execute(ExcelWriterCallback callback) throws Exception {
		Workbook workbook;
		InputStream in = null;
		if (file == null) {
			workbook = new HSSFWorkbook();
		} else {
			in = new FileInputStream(file);
			workbook = new HSSFWorkbook(in);
		}
		callback.doInPoi(workbook);
		if (in != null) {
			in.close();
		} else {
			workbook.write(out);
		}
	}
}
