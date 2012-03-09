package com.dayatang.excel;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Excel书写类。向Excel文件中填写内容
 * @author yyang
 *
 */
public class ExcelWriter {
	
	private ExcelWriterTemplate excelTemplate;

	public ExcelWriter(File file) {
		excelTemplate = new ExcelWriterTemplate(file);
	}
	
	public ExcelWriter(OutputStream out) {
		excelTemplate = new ExcelWriterTemplate(out);
	}

	public ExcelWriter(File file, OutputStream out) {
		excelTemplate = new ExcelWriterTemplate(file, out);
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
			public void doInPoi(HSSFWorkbook workbook) {
				HSSFSheet sheet;
				if (workbook.getNumberOfSheets() == 0) {
					sheet = workbook.createSheet("sheet1");
				} else {
					sheet = workbook.getSheetAt(sheetIndex);
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
			public void doInPoi(HSSFWorkbook workbook) {
				HSSFSheet sheet = workbook.getSheet(sheetName);
				if (sheet == null) {
					sheet = workbook.createSheet(sheetName);
				}
				write(sheet, rowFrom, colFrom, data);
			}
		});
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
			public void doInPoi(HSSFWorkbook workbook) {
				HSSFSheet sheet;
				if (workbook.getNumberOfSheets() == 0) {
					sheet = workbook.createSheet("sheet1");
				} else {
					sheet = workbook.getSheetAt(sheetIndex);
				}
				write(sheet, col, row, value);
			}
		});
	}	

	private void write(HSSFSheet sheet, int rowFrom, int colFrom, List<Object[]> data) {
		int row = rowFrom;
		for (Object[] dataRow : data) {
			int col = colFrom;
			for (Object dataCell : dataRow) {
				write(sheet, row, col++, dataCell);
			}
			row++;
		}
	}

	private void write(HSSFSheet sheet, int rowIndex, int colIndex, Object data) {
		HSSFRow row = sheet.createRow(rowIndex);
		HSSFCell cell = row.createCell(colIndex);
		setCellValue(cell, data);
	}

	private void setCellValue(HSSFCell cell, Object data) {
		if (data == null) {
			cell.setCellValue("");
			return;
		}
		if (data instanceof Number) {
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(((Number) data).doubleValue());
		} 
		if (data instanceof Boolean) {
			cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
			cell.setCellValue(((Boolean) data).booleanValue());
		}
		if (data instanceof Date) {
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue((Date) data);
		}
		cell.setCellValue(new HSSFRichTextString(data.toString()));
	}
}
