package com.dayatang.excel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelExporter {

	private WritableWorkbook workbook;

	public ExcelExporter(File excelFile) throws BiffException, IOException {
		workbook = Workbook.createWorkbook(excelFile);
	}

	public ExcelExporter(OutputStream out) throws BiffException, IOException {
		workbook = Workbook.createWorkbook(out);
	}

	public void exportData(String sheetName, int rowFrom, int colFrom, List<Object[]> data)
			throws RowsExceededException, WriteException {
		WritableSheet sheet = workbook.getSheet(sheetName);
		exportData(sheet, rowFrom, colFrom, data);
	}

	private void exportData(WritableSheet sheet, int rowFrom, int colFrom, List<Object[]> data)
			throws RowsExceededException, WriteException {
		int row = rowFrom;
		for (Object[] dataRow : data) {
			int col = colFrom;
			for (Object dataCell : dataRow) {
				writeValue(sheet, col++, row, dataCell);
			}
			row++;
		}
	}

	private void writeValue(WritableSheet sheet, int col, int row, Object data) throws RowsExceededException,
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

	public void close() throws WriteException, IOException {
		workbook.write();
		workbook.close();
	}

	protected WritableWorkbook getWorkbook() {
		return workbook;
	}

	public void createSheet(String... sheetName) {
		if (workbook == null) {
			return;
		}
		for (int i = 0; i < sheetName.length; i++) {
			workbook.createSheet(sheetName[i], i);
		}
	}
}
