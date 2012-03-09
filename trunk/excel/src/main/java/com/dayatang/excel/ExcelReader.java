package com.dayatang.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.BooleanCell;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelReader {
	
	private ExcelReaderTemplate excelTemplate;
	
	private ExcelReader(ExcelReaderTemplate excelTemplate) {
		this.excelTemplate = excelTemplate;
	}
	
	public static ExcelReader fromInputStream(InputStream in) throws BiffException, IOException {
		return new ExcelReader(ExcelReaderTemplate.fromInputStream(in));
	}
	
	public static ExcelReader fromClasspath(String pathname) throws BiffException, IOException {
		return new ExcelReader(ExcelReaderTemplate.fromClasspath(pathname));
	}
	
	public static ExcelReader fromFileSystem(String pathname) throws BiffException, IOException {
		return new ExcelReader(ExcelReaderTemplate.fromFileSystem(pathname));
	}
	
	public static ExcelReader fromFile(File file) throws BiffException, IOException {
		return new ExcelReader(ExcelReaderTemplate.fromFile(file));
	}
	
	
	public List<Object[]> getData(final int sheetIndex, final int rowFrom, final int colFrom, final int colTo) throws Exception {
		return excelTemplate.execute(new ExcelReaderCallback<List<Object[]>>() {

			@Override
			public List<Object[]> doInJxl(Workbook workbook) {
				return getData(workbook.getSheet(sheetIndex), rowFrom, colFrom, colTo);
			}
		});
	}

	public List<Object[]> getData(final String sheetName, final int rowFrom, final int colFrom, final int colTo) throws Exception {
		return excelTemplate.execute(new ExcelReaderCallback<List<Object[]>>() {

			@Override
			public List<Object[]> doInJxl(Workbook workbook) {
				return getData(workbook.getSheet(sheetName), rowFrom, colFrom, colTo);
			}
		});
	}
	
	/**
	 * @param sheet
	 * @param row
	 * @param colFrom
	 * @param colTo
	 * @return
	 */
	private List<Object[]> getData(Sheet sheet, int rowFrom, int colFrom, int colTo) {
		List<Object[]> results = new ArrayList<Object[]>();
		
		for (int row = rowFrom; row < sheet.getRows(); row++) {
			Object[] dataRow = new Object[colTo + colFrom + 1];
			if (sheet.getCell(0, row).getContents().isEmpty()) {
				continue;
			}
			for (int col = colFrom; col < colTo; col++) {
				Cell cell = sheet.getCell(col, row);
				CellType cellType = cell.getType();
				if (cellType.equals(CellType.DATE)) {
					dataRow[col] = ((DateCell) cell).getDate();
				} else if (cellType.equals(CellType.BOOLEAN)) {
					dataRow[col] = ((BooleanCell) cell).getValue();
				} else {
					dataRow[col] = cell.getContents();
				}
			}
			results.add(dataRow);
		}
		return results;
	}
	
	public String[] getSheetNames() throws BiffException, IOException {
		return excelTemplate.execute(new ExcelReaderCallback<String[]>() {

			@Override
			public String[] doInJxl(Workbook workbook) {
				return workbook.getSheetNames();
			}
		});
	}
	
	public String getSheetName(final int sheetIndex) throws BiffException, IOException {
		return excelTemplate.execute(new ExcelReaderCallback<String>() {

			@Override
			public String doInJxl(Workbook workbook) {
				return workbook.getSheet(sheetIndex).getName();
			}
		});
	}

	public Object readCellContents(final int sheetIndex, final int row, final int col) throws BiffException, IOException {
		return excelTemplate.execute(new ExcelReaderCallback<Object>() {

			@Override
			public String doInJxl(Workbook workbook) {
				return workbook.getSheet(sheetIndex).getCell(col, row).getContents();
			}
		});
	}

	public Object readCellContents(final String sheetName, final int row, final int col) throws BiffException, IOException {
		return excelTemplate.execute(new ExcelReaderCallback<Object>() {

			@Override
			public String doInJxl(Workbook workbook) {
				return workbook.getSheet(sheetName).getCell(col, row).getContents();
			}
		});
	}
}
