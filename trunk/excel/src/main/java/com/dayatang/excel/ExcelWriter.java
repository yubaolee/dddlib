package com.dayatang.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel书写类。向Excel文件中填写内容
 * 
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
	 * 
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
			public void doInPoi(Workbook workbook) {
				Sheet sheet;
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
	 * 
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
			public void doInPoi(Workbook workbook) throws FileNotFoundException {
				Sheet sheet = workbook.getSheet(sheetName);
				if (sheet == null) {
					sheet = workbook.createSheet(sheetName);
				}
				write(sheet, rowFrom, colFrom, data);
				exportTo(workbook, new FileOutputStream(System.getProperty("user.home") + File.separator + "test.xls"));
			}

			private void exportTo(Workbook workbook, FileOutputStream fileOutputStream) {
				FileOutputStream outputStream = null;
				try {
					outputStream = new FileOutputStream(System.getProperty("user.home") + File.separator + "test.xls");
					workbook.write(outputStream);
					outputStream.flush();
					outputStream.close();
				} catch (FileNotFoundException e) {
					System.out.println(e);
					;
				} catch (IOException e) {
					System.out.println(e);
					;
				} finally {
					if (outputStream != null) {
						try {
							outputStream.close();
							outputStream = null;
						} catch (IOException e) {
							System.out.println(e);
							;
						}
					}
				}
			}
		});
	}

	/**
	 * 向指定工作表的特定单元格填充内容
	 * 
	 * @param sheetIndex
	 * @param row
	 * @param col
	 * @param value
	 * @throws Exception
	 */
	public void write(final int sheetIndex, final int row, final int col, final Object value) throws Exception {
		excelTemplate.execute(new ExcelWriterCallback() {

			@Override
			public void doInPoi(Workbook workbook) {
				Sheet sheet;
				if (workbook.getNumberOfSheets() == 0) {
					sheet = workbook.createSheet("sheet1");
				} else {
					sheet = workbook.getSheetAt(sheetIndex);
				}
				write(sheet, col, row, value);
			}
		});
	}

	private void write(Sheet sheet, int rowFrom, int colFrom, List<Object[]> data) {
		int row = rowFrom;
		for (Object[] dataRow : data) {
			int col = colFrom;
			for (Object dataCell : dataRow) {
				write(sheet, row, col++, dataCell);
			}
			row++;
		}
	}

	private void write(Sheet sheet, int rowIndex, int colIndex, Object data) {
		System.out.println(rowIndex + ":" + colIndex + ":" + data);
		Row row = sheet.createRow(rowIndex);
		Cell cell = row.createCell(colIndex);
		// cell.setCellStyle(cellStyle);
		setCellValue(cell, data);
	}

	private void setCellValue(Cell cell, Object data) {
		if (data == null) {
			cell.setCellValue("");
			return;
		}
		if (data instanceof Number) {
			cell.setCellValue(((Number) data).doubleValue());
			return;
		}
		if (data instanceof Boolean) {
			cell.setCellValue(((Boolean) data).booleanValue());
			return;
		}
		if (data instanceof Date) {
			cell.setCellValue((Date) data);
			return;
		}
		cell.setCellValue(data.toString());
	}

	private CellStyle getCellStyle(Workbook workbook) {
		Font font = workbook.createFont();
		// 把字体颜色设置为红色
		font.setColor(Font.COLOR_NORMAL);
		// 把字体设置为粗体
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		// 创建格式
		CellStyle cellStyle = workbook.createCellStyle();
		// 把创建的字体付加于格式
		cellStyle.setFont(font);
		return cellStyle;
	}
}
