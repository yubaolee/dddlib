package com.dayatang.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import jxl.read.biff.BiffException;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Excel书写类。向Excel文件中填写内容
 * @author yyang
 *
 */
public class ExcelWriter {
	
	private ExcelWriterTemplate excelTemplate;
	
	private ExcelWriter(ExcelWriterTemplate excelTemplate) {
		this.excelTemplate = excelTemplate;
	}
	
	/**
	 * 从输出流中生成ExcelWriter
	 * @param out
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public static ExcelWriter to(OutputStream out) throws BiffException, IOException {
		return new ExcelWriter(ExcelWriterTemplate.to(out));
	}

	/**
	 * 从文件中生成ExcelWriter
	 * @param file
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public static ExcelWriter to(File file) throws BiffException, IOException {
		return new ExcelWriter(ExcelWriterTemplate.to(file));
	}
	
	public ExcelWriter setTemplate(File templateFile) throws BiffException, IOException {
		excelTemplate.setTemplate(templateFile);
		return this;
	}
	
	public ExcelWriter setTemplateFromInputStream(InputStream inputStream) throws BiffException, IOException {
		excelTemplate.setTemplate(inputStream);
		return this;
	}

	/**
	 * 用一批数据填写指定工作表中的一个区域
	 * @param sheetName
	 * @param rowFrom
	 * @param colFrom
	 * @param data
	 * @throws Exception
	 */
	public void write(final int sheetIndex, final int rowFrom, final int colFrom, final List<Object[]> data)
			throws Exception {
		excelTemplate.execute(new ExcelWriterCallback() {
			
			@Override
			public void doInJxl(WritableWorkbook workbook) throws RowsExceededException, WriteException {
				WritableSheet sheet;
				if (workbook.getNumberOfSheets() == 0) {
					sheet = workbook.createSheet("sheet1", 0);
				} else {
					sheet = workbook.getSheet(sheetIndex);
				}
				write(sheet, rowFrom, colFrom, data);
			}
		});
	}
	
	/**
	 * 用一批数据填写指定工作表中的一个区域
	 * @param sheetName
	 * @param rowFrom
	 * @param colFrom
	 * @param data
	 * @throws Exception
	 */
	public void write(final String sheetName, final int rowFrom, final int colFrom, final List<Object[]> data)
			throws Exception {
		excelTemplate.execute(new ExcelWriterCallback() {
			
			@Override
			public void doInJxl(WritableWorkbook workbook) throws RowsExceededException, WriteException {
				WritableSheet sheet;
				if (workbook.getNumberOfSheets() == 0) {
					sheet = workbook.createSheet(sheetName, 0);
				} else {
					sheet = workbook.getSheet(sheetName);
				}
				write(sheet, rowFrom, colFrom, data);
			}
		});
	}

	private void write(WritableSheet sheet, int rowFrom, int colFrom, List<Object[]> data)
			throws RowsExceededException, WriteException {
		int row = rowFrom;
		for (Object[] dataRow : data) {
			int col = colFrom;
			for (Object dataCell : dataRow) {
				write(sheet, row, col++, dataCell);
			}
			row++;
		}
	}

	private void write(WritableSheet sheet, int row, int col, Object data) throws RowsExceededException,
			WriteException {
		if (data == null) {
			sheet.addCell(new Label(col, row, ""));
		}
		if (data instanceof String) {
			sheet.addCell(new Label(col, row, (String) data));
			return;
		}
		if (data instanceof Number) {
			sheet.addCell(new jxl.write.Number(col, row, Double.parseDouble(data.toString())));
			return;
		}
		if (data instanceof Date) {
			sheet.addCell(new DateTime(col, row, (Date) data, new WritableCellFormat(new DateFormat("yyyy-MM-dd"))));
			return;
		}
		sheet.addCell(new Label(col, row, (data == null ? "" : data.toString())));
	}

	/**
	 * 向指定工作表的特定单元格填充内容
	 * @param sheetIndex
	 * @param row
	 * @param col
	 * @param value
	 * @throws Exception
	 */
	public void writeCellContents(final int sheetIndex, final int row, final int col, final Object value) throws Exception {
		excelTemplate.execute(new ExcelWriterCallback() {

			@Override
			public void doInJxl(WritableWorkbook workbook) throws RowsExceededException, WriteException {
				WritableSheet sheet = workbook.getSheet(sheetIndex);
				write(sheet, col, row, value);
			}
		});
	}	
}
