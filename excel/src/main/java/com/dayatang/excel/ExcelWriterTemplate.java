package com.dayatang.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
	
	private Workbook in;
	private OutputStream out;

	public ExcelWriterTemplate(File outputFile) throws FileNotFoundException {
		out = new FileOutputStream(outputFile);
	}
	
	public ExcelWriterTemplate(OutputStream out) {
		this.out = out;
	}

	public void setSource(File sourceFile) throws FileNotFoundException, IOException {
		in = WorkbookFactory.createWorkbook(sourceFile);
	}
	
	public void setSource(InputStream in, Class<? extends Workbook> docType) throws IOException {
		this.in = WorkbookFactory.createWorkbook(in, docType);
	}
	
	public void execute(ExcelWriterCallback callback) throws Exception {
		if (in == null) {
			in = new HSSFWorkbook();
		}
		callback.doInPoi(in);
		in.write(out);
	}
}
