package com.dayatang.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import jxl.BooleanCell;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Excel读取工具
 * @author yyang
 *
 */
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
	
	/**
	 * 将工作表的一个区域的内容读出到一个对象数组的列表中。行的范围是：从rowFrom开始，到遇到的第一个空行结束。
	 * 列的范围是：从colFrom开始，取colCount行
	 * @param sheetIndex
	 * @param rowFrom
	 * @param colFrom
	 * @param colTo
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> read(final int sheetIndex, final int rowFrom, final int colFrom, final int colCount) throws Exception {
		return excelTemplate.execute(new ExcelReaderCallback<List<Object[]>>() {

			@Override
			public List<Object[]> doInJxl(Workbook workbook) {
				return read(workbook.getSheet(sheetIndex), rowFrom, colFrom, colCount).getData();
			}
		});
	}
	
	/**
	 * 将工作表的一个区域的内容读出到一个对象数组的列表中。行的范围是：从rowFrom开始，取rowCount行。
	 * 列的范围是：从colFrom开始，取colCount行
	 * @param sheetIndex
	 * @param rowFrom
	 * @param colFrom
	 * @param rowCount
	 * @param colCount
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> read(final int sheetIndex, final int rowFrom, final int colFrom, final int rowCount, final int colCount) throws Exception {
		return excelTemplate.execute(new ExcelReaderCallback<List<Object[]>>() {
			@Override
			public List<Object[]> doInJxl(Workbook workbook) {
				return read(workbook.getSheet(sheetIndex), rowFrom, colFrom, rowCount, colCount).getData();
			}
		});
	}

	/**
	 * 将工作表的一个区域的内容读出到一个对象数组的列表中。行的范围是：从rowFrom开始，到遇到的第一个空行结束。
	 * 列的范围是：从colFrom开始，取colCount行
	 * @param sheetName
	 * @param rowFrom
	 * @param colFrom
	 * @param colCount
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> read(final String sheetName, final int rowFrom, final int colFrom, final int colCount) throws Exception {
		return excelTemplate.execute(new ExcelReaderCallback<List<Object[]>>() {

			@Override
			public List<Object[]> doInJxl(Workbook workbook) {
				return read(workbook.getSheet(sheetName), rowFrom, colFrom, colCount).getData();
			}
		});
	}

	/**
	 * 将工作表的一个区域的内容读出到一个对象数组的列表中。行的范围是：从rowFrom开始，取rowCount行。
	 * 列的范围是：从colFrom开始，取colCount行
	 * @param sheetName
	 * @param rowFrom
	 * @param colFrom
	 * @param rowCount
	 * @param colCount
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> read(final String sheetName, final int rowFrom, final int colFrom, final int rowCount, final int colCount) throws Exception {
		return excelTemplate.execute(new ExcelReaderCallback<List<Object[]>>() {

			@Override
			public List<Object[]> doInJxl(Workbook workbook) {
				return read(workbook.getSheet(sheetName), rowFrom, colFrom, rowCount, colCount).getData();
			}
		});
	}

	/**
	 * 读取工作表中指定单元格的值（如果单元格中的是公式，取运算结果）。
	 * @param sheetIndex
	 * @param row
	 * @param col
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public Object readCellValue(final int sheetIndex, final int row, final int col) throws BiffException, IOException {
		return excelTemplate.execute(new ExcelReaderCallback<Object>() {

			@Override
			public Object doInJxl(Workbook workbook) {
				return getCellValue(workbook.getSheet(sheetIndex).getCell(col, row));
			}
		});
	}

	/**
	 * 读取工作表中指定单元格的值（如果单元格中的是公式，取运算结果）。
	 * @param sheetName
	 * @param row
	 * @param col
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public Object readCellValue(final String sheetName, final int row, final int col) throws BiffException, IOException {
		return excelTemplate.execute(new ExcelReaderCallback<Object>() {

			@Override
			public Object doInJxl(Workbook workbook) {
				return getCellValue(workbook.getSheet(sheetName).getCell(col, row));
			}
		});
	}

	/**
	 * 获取工作簿中所有工作表名字的数组
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public String[] getSheetNames() throws BiffException, IOException {
		return excelTemplate.execute(new ExcelReaderCallback<String[]>() {

			@Override
			public String[] doInJxl(Workbook workbook) {
				return workbook.getSheetNames();
			}
		});
	}
	
	/**
	 * 获取工作簿中第sheetIndex个
	 * @param sheetIndex
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public String getSheetName(final int sheetIndex) throws BiffException, IOException {
		return excelTemplate.execute(new ExcelReaderCallback<String>() {

			@Override
			public String doInJxl(Workbook workbook) {
				return workbook.getSheet(sheetIndex).getName();
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
	private SheetRange read(final Sheet sheet, final int rowFrom, final int colFrom, final int rowCount, final int colCount) {
		int colTo = colFrom + colCount;
		SheetRange result = new SheetRange(rowCount, colCount);
		for (int rowIndex = rowFrom; rowIndex < rowCount; rowIndex++) {
			for (int colIndex = colFrom; colIndex < colTo; colIndex++) {
				Cell cell = sheet.getCell(colIndex, rowIndex);
				result.addData(getCellValue(cell));
			}
		}
		return result;
	}
	
	private Object getCellValue(Cell cell) {
		CellType cellType = cell.getType();
		if (cellType.equals(CellType.DATE)) {
			return ((DateCell) cell).getDate();
		} else if (cellType.equals(CellType.BOOLEAN)) {
			return ((BooleanCell) cell).getValue();
		} else if (cellType.equals(CellType.NUMBER)) {
			return ((NumberCell) cell).getValue();
		}
		return cell.getContents();
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
		for (int rowIndex = rowFrom; rowIndex < sheet.getRows(); rowIndex++) {
			boolean isBlank = true;
			for (int colIndex = colFrom; colIndex < colTo; colIndex++) {
				if (StringUtils.isNotEmpty(sheet.getCell(colIndex, rowIndex).getContents())) {
					result++;
					isBlank = false;
					break;
				}
			}
			if (isBlank) return result;
		}
		return result;
	}
}
