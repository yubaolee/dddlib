package com.dayatang.excel;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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
	private Workbook workbook;
	private Version version;
	
	
	public ExcelReader(File excelFile) {
		workbook = WorkbookFactory.createWorkbook(excelFile);
		this.version = Version.of(excelFile.getName());
	}
	
	public ExcelReader(File excelFile, Version version) {
		workbook = WorkbookFactory.createWorkbook(excelFile, version);
		this.version = version;
	}
	
	public ExcelReader(InputStream excelStream, Version version) {
		workbook = WorkbookFactory.createWorkbook(excelStream, version);
		this.version = version;
	}
	
	public ExcelRangeData read(int sheetIndex, ExcelRange excelRange) {
		if (sheetIndex <0 || sheetIndex >= workbook.getNumberOfSheets()) {
			throw new IllegalStateException("Sheet index of " + sheetIndex + " not exists!");
		}
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		List<Object[]> data = readRange(sheet, excelRange);
		return new ExcelRangeData(data, version, isDate1904(workbook));
	}
	
	public ExcelRangeData read(String sheetName, ExcelRange excelRange) {
		Sheet sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			throw new IllegalStateException("Sheet name of " + sheetName + " not exists!");
		}
		List<Object[]> data = readRange(sheet, excelRange);
		return new ExcelRangeData(data, version, isDate1904(workbook));
	}
	
	public Object read(int sheetIndex, int rowIndex, int columnIndex) {
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		return getCellValue(sheet.getRow(rowIndex).getCell(columnIndex));
	}
	
	public Object read(String sheetName, int rowIndex, int columnIndex) {
		Sheet sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			throw new IllegalStateException("Sheet name of " + sheetName + " not exists!");
		}
		return getCellValue(sheet.getRow(rowIndex).getCell(columnIndex));
	}
	
	public Object read(int sheetIndex, int rowIndex, String columnName) {
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		return getCellValue(sheet.getRow(rowIndex).getCell(convertColumnLabelToIndex(columnName)));
	}
	
	public Object read(String sheetName, int rowIndex, String columnName) {
		Sheet sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			throw new IllegalStateException("Sheet name of " + sheetName + " not exists!");
		}
		return getCellValue(sheet.getRow(rowIndex).getCell(convertColumnLabelToIndex(columnName)));
	
	}
	/**
	 * 检测Excel工作簿是否采用1904日期系统
	 */
	private boolean isDate1904(Workbook workbook) {
        Sheet sheet = workbook.createSheet();
        int sheetIndex = workbook.getSheetIndex(sheet);
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(0.0);
        boolean is1994 = isDate1904(cell);
        workbook.removeSheetAt(sheetIndex);
        return is1994;
	}
	
    /**
     * throws an exception for non-numeric cells
     */
	private static boolean isDate1904(Cell cell) {
        double value = cell.getNumericCellValue();
        Date date = cell.getDateCellValue();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        long year1900 = cal.get(Calendar.YEAR)-1900;
        long yearEst1900 = Math.round(value/(365.25));
        return year1900 > yearEst1900;
    }

	private List<Object[]> readRange(Sheet sheet, ExcelRange excelRange) {
		List<Object[]> results = new ArrayList<Object[]>();
		int lastRow = excelRange.getRowTo() < 0 ? getLastRowNum(sheet, excelRange) : excelRange.getRowTo();
		System.out.println("===============" + lastRow);
		if (lastRow < excelRange.getRowFrom()) {	//没有数据
			return results;
		}
		
		for (int rowIndex = excelRange.getRowFrom(); rowIndex <= lastRow; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			Object[] rowData = new Object[excelRange.getColumnTo() - excelRange.getColumnFrom() + 1];
			for (int columnIndex = excelRange.getColumnFrom(); columnIndex <= excelRange.getColumnTo(); columnIndex++) {
				Cell cell = row.getCell(columnIndex, Row.CREATE_NULL_AS_BLANK);
				rowData[columnIndex - excelRange.getColumnFrom()] = getCellValue(cell);
			}
			results.add(rowData);
		}
		return results;
	}

	private int getLastRowNum(Sheet sheet, ExcelRange excelRange) {
		int lastRowNum = sheet.getLastRowNum();
		for (int row = excelRange.getRowFrom(); row <= lastRowNum; row++) {
			boolean isBlankRow = true;
			for (int column = excelRange.getColumnFrom(); column <= excelRange.getColumnTo(); column++) {
				Object cellValue = getCellValue(sheet.getRow(row).getCell(column));
				if (cellValue != null && StringUtils.isNotBlank(cellValue.toString())) { //本行非空行，检验下一行
					isBlankRow = false;
					break;
				}
				
			}
			if (isBlankRow) { //代码进入此处说明整行为空行，
				return row - 1;
			}
		}
		return lastRowNum;
	}

	private Object getCellValue(Cell cell) {
		try{
			if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
				LOGGER.error("Cell content is error. Sheet: {}, row: {}, column: {}", new Object[] {cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex()});
				return null;
			}
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return null;
			}
			if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				return cell.getBooleanCellValue();
			}
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return cell.getNumericCellValue();
			}
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			}
		} catch (IllegalStateException e) {
			LOGGER.error(e.getLocalizedMessage());
			LOGGER.error("Read cell error. Sheet: {}, row: {}, column: {}", new Object[] {cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex()});
			throw new RuntimeException(e);
		}
		return null;
	}

	private int convertColumnLabelToIndex(String columnLabel) {
		if (columnLabel.length() > 2) {
			throw new IllegalArgumentException("Column index too large!");
		}
		String theColumn = columnLabel.toUpperCase();
		if (theColumn.length() == 1) {
			int letter = theColumn.charAt(0);
			return letter - 65;
		}
		int firstLetter = theColumn.charAt(0);
		int lastLetter = theColumn.charAt(1);
		return (firstLetter - 64) * 26 + lastLetter - 65;
	}

}
