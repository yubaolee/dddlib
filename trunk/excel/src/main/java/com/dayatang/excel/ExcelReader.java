package com.dayatang.excel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel读取工具
 * 
 * @author yyang
 * 
 */
public class ExcelReader {
	private ExcelReaderTemplate readerTemplate;

	public ExcelReader(InputStream in, Version version) {
		readerTemplate = new ExcelReaderTemplate(in, version);
	}

	public ExcelReader(File excelFile) {
		readerTemplate = new ExcelReaderTemplate(excelFile);
	}
	
	public List<Object[]> read(final ReadingRange range) {
		
		return readerTemplate.execute(new ExcelReaderCallback<List<Object[]>>() {

			@Override
			public List<Object[]> doInPoi(Workbook workbook) {
				Sheet sheet = range.getSheetIndex() < 0 ? workbook.getSheet(range.getSheetName()) : workbook.getSheetAt(range.getSheetIndex());
				return readRange(sheet, range.getRowFrom(), range.getRowTo(), range.getColumnIndexes(), range.getColumnTypes());
			}
		});
	}

	/**
	 * 读取工作表中指定单元格的值（如果单元格中的是公式，取运算结果）。
	 * 
	 * @param sheetIndex
	 * @param row
	 * @param col
	 * @return
	 */
	public Object readCellValue(final int sheetIndex, final int row, final int col, final DataType dataType) {
		return readerTemplate.execute(new ExcelReaderCallback<Object>() {

			@Override
			public Object doInPoi(Workbook workbook) {
				return getCellValue(workbook.getSheetAt(sheetIndex).getRow(row).getCell(col), dataType);
			}
		});
	}

	/**
	 * 读取工作表中指定单元格的值（如果单元格中的是公式，取运算结果）。
	 * 
	 * @param sheetName
	 * @param row
	 * @param col
	 * @return
	 */
	public Object readCellValue(final String sheetName, final int row, final int col, final DataType dataType) {
		return readerTemplate.execute(new ExcelReaderCallback<Object>() {

			@Override
			public Object doInPoi(Workbook workbook) {
				return getCellValue(workbook.getSheet(sheetName).getRow(row).getCell(col), dataType);
			}
		});
	}

	private List<Object[]> readRange(Sheet sheet, int rowFrom, int rowTo, int[] columnIndexes, DataType[] columnTypes) {
		List<Object[]> results = new ArrayList<Object[]>();
		int colCount = columnIndexes.length;
		int lastRow = rowTo < 0 ? sheet.getLastRowNum() : rowTo;
		for (int rowIndex = rowFrom; rowIndex <= lastRow; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			Object[] rowData = new Object[colCount];
			for (int i = 0; i < colCount; i++) {
				Cell cell = row.getCell(columnIndexes[i], Row.CREATE_NULL_AS_BLANK);
				rowData[i] = getCellValue(cell, columnTypes[i]);
			}
			results.add(rowData);
		}
		return results;
	}

	private Object getCellValue(Cell cell, DataType dataType) {
		try{
			if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
				System.out.println("Error cell, row: " + cell.getRowIndex() + ", column: " + cell.getColumnIndex());
				return null;
			}
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return null;
			}
			if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				return cell.getBooleanCellValue();
			}
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return dataType == DataType.DATE ? cell.getDateCellValue() : cell.getNumericCellValue();
			}
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			}
		} catch (IllegalStateException e) {
			System.out.println(e.getLocalizedMessage() + ", row: " + cell.getRowIndex() + ", column: " + cell.getColumnIndex());
			throw new RuntimeException(e);
		}
		return null;
	}

}
