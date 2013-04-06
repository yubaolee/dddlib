package com.dayatang.excel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
	private ExcelReaderTemplate readerTemplate;
	private Version version;

	private int sheetIndex;
	private String sheetName;
	private int rowFrom, rowTo = -1;
	private int columnFrom, columnTo;
	
	public ExcelReader(Builder builder) {
		this.readerTemplate = builder.readerTemplate;
		this.version = builder.version;
		this.sheetIndex = builder.sheetIndex;
		this.sheetName = builder.sheetName;
		this.rowFrom = builder.rowFrom;
		this.rowTo = builder.rowTo;
		this.columnFrom = builder.columnFrom;
		this.columnTo = builder.columnTo;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public ExcelRangeData read() {
		
		return readerTemplate.execute(new ExcelReaderCallback<ExcelRangeData>() {

			@Override
			public ExcelRangeData doInPoi(Workbook workbook) {
				Sheet sheet = getSheet(workbook);
				List<Object[]> data = readRange(sheet);
				return new ExcelRangeData(data, version, isDate1904(workbook));
			}

			private Sheet getSheet(Workbook workbook) {
				Sheet result = null;
				if (StringUtils.isNotBlank(sheetName)) {
					result = workbook.getSheet(sheetName);
					if (result == null) {
						throw new IllegalStateException("Sheet '" + sheetName + "' not exists!");
					}
				} else {
					result = workbook.getSheetAt(sheetIndex);
					if (result == null) {
						throw new IllegalStateException("Sheet index of " + sheetIndex + " not exists!");
					}
				}
				return result;
			}

		});
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

	private List<Object[]> readRange(Sheet sheet) {
		List<Object[]> results = new ArrayList<Object[]>();
		int lastRow = rowTo < 0 ? getLastRowNum(sheet) : rowTo;
		System.out.println("===============" + lastRow);
		if (lastRow < rowFrom) {	//没有数据
			return results;
		}
		
		for (int rowIndex = rowFrom; rowIndex <= lastRow; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			Object[] rowData = new Object[columnTo - columnFrom + 1];
			for (int columnIndex = columnFrom; columnIndex <= columnTo; columnIndex++) {
				Cell cell = row.getCell(columnIndex, Row.CREATE_NULL_AS_BLANK);
				rowData[columnIndex - columnFrom] = getCellValue(cell);
			}
			results.add(rowData);
		}
		return results;
	}

	private int getLastRowNum(Sheet sheet) {
		int lastRowNum = sheet.getLastRowNum();
		for (int row = rowFrom; row <= lastRowNum; row++) {
			boolean isBlankRow = true;
			for (int column = columnFrom; column <= columnTo; column++) {
				if (StringUtils.isNotBlank(sheet.getRow(row).getCell(column).getStringCellValue())) { //本行非空行，检验下一行
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

	public static class Builder {
		private ExcelReaderTemplate readerTemplate;
		private Version version;
		private int sheetIndex = -1;
		private String sheetName;
		private int rowFrom = -1, rowTo = -1;
		private int columnFrom, columnTo;

		public Builder file(File excelFile) {
			readerTemplate = new ExcelReaderTemplate(excelFile);
			version = Version.of(excelFile.getName());
			return this;
		}
		
		public Builder inputStream(InputStream in, Version version) {
			readerTemplate = new ExcelReaderTemplate(in, version);
			this.version = version;
			return this;
		}
		
		public Builder sheetAt(int sheetIndex) {
			this.sheetIndex = sheetIndex;
			this.sheetName = null;
			return this;
		}
		
		public Builder sheetName(String sheetName) {
			this.sheetName = sheetName;
			this.sheetIndex = -1;
			return this;
		}
		 
		public Builder rowFrom(int rowFrom) {
			this.rowFrom = rowFrom;
			return this;
		}
		
		public Builder rowTo(int rowTo) {
			this.rowTo = rowTo;
			return this;
		}

		public Builder columnRange(int columnFrom, int columnTo) {
			if (columnTo < columnFrom) {
				throw new IllegalArgumentException("Last column is less than first column!");
			}
			this.columnFrom = columnFrom;
			this.columnTo = columnTo;
			return this;
		}

		public Builder columnRange(String columnFrom, String columnTo) {
			return columnRange(convertColumnLabelToIndex(columnFrom), convertColumnLabelToIndex(columnTo));
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
		
		public ExcelReader build() {
			if (sheetIndex < 0 && StringUtils.isEmpty(sheetName)) {
				throw new IllegalArgumentException("Sheet name or index needed!");
			}
			if (rowFrom < 0) {
				throw new IllegalArgumentException("First row is less than 0!");
			}
			if (rowTo >= 0 && rowTo < rowFrom) {
				throw new IllegalArgumentException("Last row is less than first row!");
			}
			if (columnFrom < 0) {
				throw new IllegalArgumentException("Need one column at least!");
			}
			return new ExcelReader(this);
		}
	}

}
