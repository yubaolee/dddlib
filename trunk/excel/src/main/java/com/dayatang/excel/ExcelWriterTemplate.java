package com.dayatang.excel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	
	private Workbook in;
	
	//执行Excel操作后是否需要关闭fileStream
	private boolean needCloseStream = false;

	private ExcelWriterTemplate(File excelFile) throws BiffException, IOException {
		out = new BufferedOutputStream(new FileOutputStream(excelFile));
	}

	private ExcelWriterTemplate(OutputStream out) throws BiffException, IOException {
		this.out = new BufferedOutputStream(out);
		
	}
	
	public static ExcelWriterTemplate toOutputStream(OutputStream out) throws BiffException, IOException {
		return new ExcelWriterTemplate(out);
	}

	public static ExcelWriterTemplate toFile(File file) throws BiffException, IOException {
		if (!file.exists()) {
			file.mkdirs();
			//throw new FileNotFoundException("File '" + file + "' not found!");
		}
		ExcelWriterTemplate result =  new ExcelWriterTemplate(file);
		result.needCloseStream = true;
		return result;
	}

	public void execute(ExcelWriterCallback callback) throws Exception {
		WritableWorkbook workbook;
		if (in == null) {
			workbook = Workbook.createWorkbook(out);
		} else {
			workbook = Workbook.createWorkbook(out, in);
			//workbook = Workbook.createWorkbook(out);
		}
		//callback.doInJxl(workbook);
		workbook.write();
		workbook.close();
		if (needCloseStream) {
			out.close();
		}
	}
	
	public ExcelWriterTemplate setTemplateFromFile(File templateFile) throws BiffException, IOException {
		in = Workbook.getWorkbook(templateFile);
		return this;
	}
	
	public ExcelWriterTemplate setTemplateFromClasspath(String pathname) throws BiffException, IOException {
		in = Workbook.getWorkbook(getClass().getResourceAsStream(pathname));
		return this;
	}
	
	public ExcelWriterTemplate setTemplateFromFileSystem(String pathname) throws BiffException, IOException {
		in = Workbook.getWorkbook(new File(pathname));
		return this;
	}
	
	public ExcelWriterTemplate setTemplateFromInputStream(InputStream inputStream) throws BiffException, IOException {
		in = Workbook.getWorkbook(inputStream);
		return this;
	}

}
