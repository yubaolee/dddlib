package com.dayatang.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel读取工具
 * 
 * @author yyang
 * 
 */
public class ExcelReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);
	private ExcelReaderTemplate readerTemplate;

	public ExcelReader(InputStream in, Version version) throws IOException {
		readerTemplate = new ExcelReaderTemplate(in, version);
	}

	public ExcelReader(File excelFile) throws FileNotFoundException, IOException {
		readerTemplate = new ExcelReaderTemplate(excelFile);
	}
	
	public List<Object[]> read(final ReadingRange range) throws Exception {
		
		return readerTemplate.execute(new ExcelReaderCallback<List<Object[]>>() {

			@Override
			public List<Object[]> doInPoi(Workbook workbook) {
				Sheet sheet = workbook.getSheetAt(range.getSheetIndex());
				return read(sheet, range.getRowFrom(), range.getRowTo(), range.getColumns(), range.getDataTypes());
			}
		});
	}

	/**
	 * 将工作表的一个区域的内容读出到一个对象数组的列表中。行的范围是：从rowFrom开始，到遇到的第一个空行结束。
	 * 列的范围是：从colFrom开始，取colCount行
	 * 
	 * @param sheetIndex
	 * @param rowFrom
	 * @param colFrom
	 * @param colTo
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> read(final int sheetIndex, final int rowFrom, final int colFrom, final int colCount) throws Exception {
		
		return readerTemplate.execute(new ExcelReaderCallback<List<Object[]>>() {

			@Override
			public List<Object[]> doInPoi(Workbook workbook) {
				return read(workbook.getSheetAt(sheetIndex), rowFrom, colFrom, colCount).getData();
			}
		});
	}

	/**
	 * 将工作表的一个区域的内容读出到一个对象数组的列表中。行的范围是：从rowFrom开始，取rowCount行。
	 * 列的范围是：从colFrom开始，取colCount行
	 * 
	 * @param sheetIndex
	 * @param rowFrom
	 * @param colFrom
	 * @param rowCount
	 * @param colCount
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> read(final int sheetIndex, final int rowFrom, final int colFrom, final int rowCount, final int colCount) throws Exception {
		return readerTemplate.execute(new ExcelReaderCallback<List<Object[]>>() {
			@Override
			public List<Object[]> doInPoi(Workbook workbook) {
				
				
				
				
				return read(workbook.getSheetAt(sheetIndex), rowFrom, colFrom, rowCount, colCount).getData();
			}
		});
	}

	/**
	 * 将工作表的一个区域的内容读出到一个对象数组的列表中。行的范围是：从rowFrom开始，到遇到的第一个空行结束。
	 * 列的范围是：从colFrom开始，取colCount行
	 * 
	 * @param sheetName
	 * @param rowFrom
	 * @param colFrom
	 * @param colCount
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> read(final String sheetName, final int rowFrom, final int colFrom, final int colCount) throws Exception {
		return readerTemplate.execute(new ExcelReaderCallback<List<Object[]>>() {

			@Override
			public List<Object[]> doInPoi(Workbook workbook) {
				return read(workbook.getSheet(sheetName), rowFrom, colFrom, colCount).getData();
			}
		});
	}

	/**
	 * 将工作表的一个区域的内容读出到一个对象数组的列表中。行的范围是：从rowFrom开始，取rowCount行。
	 * 列的范围是：从colFrom开始，取colCount行
	 * 
	 * @param sheetName
	 * @param rowFrom
	 * @param colFrom
	 * @param rowCount
	 * @param colCount
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> read(final String sheetName, final int rowFrom, final int colFrom, final int rowCount, final int colCount) throws Exception {
		return readerTemplate.execute(new ExcelReaderCallback<List<Object[]>>() {

			@Override
			public List<Object[]> doInPoi(Workbook workbook) {
				return read(workbook.getSheet(sheetName), rowFrom, colFrom, rowCount, colCount).getData();
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
	 * @throws BiffException
	 * @throws IOException
	 */
	public Object readCellValue(final int sheetIndex, final int row, final int col) throws Exception {
		return readerTemplate.execute(new ExcelReaderCallback<Object>() {

			@Override
			public Object doInPoi(Workbook workbook) {
				return getCellValue(workbook.getSheetAt(sheetIndex).getRow(row).getCell(col));
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
	 * @throws BiffException
	 * @throws IOException
	 */
	public Object readCellValue(final String sheetName, final int row, final int col) throws Exception {
		return readerTemplate.execute(new ExcelReaderCallback<Object>() {

			@Override
			public Object doInPoi(Workbook workbook) {
				return getCellValue(workbook.getSheet(sheetName).getRow(row).getCell(col));
			}
		});
	}

	/**
	 * 获取工作簿中第sheetIndex个
	 * 
	 * @param sheetIndex
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public String getSheetName(final int sheetIndex) throws Exception {
		return readerTemplate.execute(new ExcelReaderCallback<String>() {

			@Override
			public String doInPoi(Workbook workbook) {
				return workbook.getSheetAt(sheetIndex).getSheetName();
			}
		});
	}

	private Object getCellValue(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return null;
		}
		return getCellValue(cell, cell.getCellType());
	}

	private Object getCellValue(Cell cell, int cellType) {
		switch (cellType) {
		case Cell.CELL_TYPE_BLANK:
			return null;
		case Cell.CELL_TYPE_ERROR:
			LOGGER.error("Error cell found. workbook: {}, sheet: {}, row: {}, column: {}",
					new Object[] { cell.getRow().getSheet().getWorkbook().toString(), cell.getRow().getSheet().getSheetName(), cell.getRowIndex(),
							cell.getColumnIndex() });
		case Cell.CELL_TYPE_FORMULA:
			return getCellValue(cell, cell.getCachedFormulaResultType());
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		default:
			return cell.getRichStringCellValue();
		}
	}

	private Object getCellValue(Cell cell, DataType dataType) {
		if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return null;
		}
		if (dataType == DataType.BOOLEAN) {
			return cell.getBooleanCellValue();
		}
		if (dataType == DataType.DATE) {
			return cell.getDateCellValue();
		}
		if (dataType == DataType.NUMERIC) {
			return cell.getStringCellValue();
		}
		if (dataType == DataType.STRING) {
			return cell.getStringCellValue();
		}
		return null;
	}

	/**
	 * @param sheet
	 * @param row
	 * @param colFrom
	 * @param colTo
	 * @return
	 */
	private SheetRange read(final Sheet sheet, final int rowFrom, final int colFrom, final int colCount) {
		int rowCount = getRowCount(sheet, rowFrom, colFrom, colFrom + colCount);
		return read(sheet, rowFrom, colFrom, rowCount, colCount);
	}

	private int getRowCount(Sheet sheet, int rowFrom, int colFrom, int colTo) {
		int result = 0;
		for (int rowIndex = rowFrom; rowIndex < sheet.getLastRowNum(); rowIndex++) {
			boolean isBlank = true;
			for (int colIndex = colFrom; colIndex < colTo; colIndex++) {
				if (StringUtils.isNotEmpty(sheet.getRow(rowIndex).getCell(colIndex).getStringCellValue())) {
					result++;
					isBlank = false;
					break;
				}
			}
			if (isBlank)
				return result;
		}
		return result;
	}

	/**
	 * @param sheet
	 * @param row
	 * @param colFrom
	 * @param colTo
	 * @return
	 */
	private SheetRange read(final Sheet sheet, final int rowFrom, final int colFrom, final int rowCount, final int colCount) {
		int colTo = colFrom + colCount;
		SheetRange result = new SheetRange(rowCount, colCount);
		for (int rowIndex = rowFrom; rowIndex < rowCount; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			for (int colIndex = colFrom; colIndex < colTo; colIndex++) {
				Cell cell = row.getCell(colIndex);
				result.addData(getCellValue(cell));
			}
		}
		return result;
	}

	private List<Object[]> read(Sheet sheet, int rowFrom, int rowTo, int[] columns, DataType[] dataTypes) {
		List<Object[]> results = new ArrayList<Object[]>();
		int colCount = columns.length;
		int lastRow = rowTo < 0 ? getLastRow(sheet, rowFrom, columns) : rowTo;
		for (int rowIndex = rowFrom; rowIndex <= lastRow; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			Object[] rowData = new Object[colCount];
			for (int i = 0; i < colCount; i++) {
				Cell cell = row.getCell(columns[i], Row.CREATE_NULL_AS_BLANK);
				rowData[i] = dataTypes == null ?  getCellValue(cell) : getCellValue(cell, dataTypes[i]);
			}
			results.add(rowData);
		}
		return results;
	}

	private int getLastRow(Sheet sheet, int rowFrom, int[] columns) {
		int rowIndex = rowFrom;
		int lastRow = sheet.getLastRowNum();
		while(rowIndex <= lastRow) {
			Row row = sheet.getRow(rowIndex);
			for (int colIndex : columns) {
				if (row.getCell(colIndex, Row.CREATE_NULL_AS_BLANK).getCellType() != Cell.CELL_TYPE_BLANK) {
					rowIndex++;
					break;
				}
			}
			return rowIndex;
		}
		return lastRow;
	}
}
