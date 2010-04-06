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

public class ExcelImporter {
	private Workbook workbook;

	public ExcelImporter(File excelFile) throws BiffException, IOException {
		workbook = Workbook.getWorkbook(excelFile);
	}

	public ExcelImporter(InputStream in) throws BiffException, IOException {
		workbook = Workbook.getWorkbook(in);
	}

	public List<Object[]> getData(int sheetIndex, int rowFrom, int colFrom, int colTo) throws Exception {
		return getData(workbook.getSheet(sheetIndex), rowFrom, colFrom, colTo);
	}

	public List<Object[]> getData(String sheetName, int rowFrom, int colFrom, int colTo) throws Exception {
		return getData(workbook.getSheet(sheetName), rowFrom, colFrom, colTo);
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
	
	public void close() {
		workbook.close();
	}

	protected Workbook getWorkbook() {
		return workbook;
	}
 }
