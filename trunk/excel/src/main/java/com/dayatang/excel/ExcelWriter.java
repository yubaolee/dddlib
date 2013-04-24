package com.dayatang.excel;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
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

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private ExcelWriterTemplate writerTemplate;

	private int sheetIndex;
	private String sheetName;
	private int rowFrom;
	private int columnFrom;

	public ExcelWriter(Builder builder) {
		this.writerTemplate = builder.writerTemplate;
		this.sheetIndex = builder.sheetIndex;
		this.sheetName = builder.sheetName;
		this.rowFrom = builder.rowFrom;
		this.columnFrom = builder.columnFrom;
	}
	
	public static Builder builder(File outputFile) {
		Builder builder = new Builder();
		builder.writerTemplate = new ExcelWriterTemplate(outputFile);
		return builder;
	}
	
	public static Builder builder(OutputStream out) {
		Builder builder = new Builder();
		builder.writerTemplate = new ExcelWriterTemplate(out);
		return builder;
	}

	public void setTemplateFile(File templateFile) {
		writerTemplate.setTemplateFile(templateFile);
	}
	
	public void setTemplateFile(InputStream in, Version version) {
		writerTemplate.setTemplateFile(in, version);
	}
	
	public void write(final List<Object[]> data) {
		writerTemplate.execute(new ExcelWriterCallback() {

			@Override
			public void doInPoi(Workbook workbook) {
				Sheet sheet = getSheet(workbook, sheetIndex, sheetName);
				write(sheet, rowFrom, columnFrom, data);
			}
		});
	}
	
	private Sheet getSheet(Workbook workbook, int sheetIndex, String sheetName) {
		Sheet sheet = null;
		if (workbook.getNumberOfSheets() == 0) {
			sheet = workbook.createSheet("sheet1");
		}
		if (sheetIndex >= 0) {
			sheet = workbook.getSheetAt(sheetIndex);
			if (sheet == null) {
	            throw new IllegalArgumentException("Sheet index ("
	                    + sheetIndex +") is out of range (0.." + (workbook.getNumberOfSheets() - 1) + ")");
			}
		}
		sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
            throw new IllegalArgumentException("Sheet name ("
                    + sheetName +") does not exists.)");
		}
		return sheet;
	}

	private void write(Sheet sheet, int rowFrom, int colFrom, List<Object[]> data) {
		int rowIndex = rowFrom;
		for (Object[] dataRow : data) {
			Row row = sheet.createRow(rowIndex);
			int colIndex = colFrom;
			for (Object dataCell : dataRow) {
				Cell cell = row.createCell(colIndex);
				setCellValue(cell, dataCell);
				colIndex++;
			}
			rowIndex++;
		}
	}

	
	public void writeCell(final Object value) {
		writerTemplate.execute(new ExcelWriterCallback() {

			@Override
			public void doInPoi(Workbook workbook) {
				Sheet sheet = getSheet(workbook, sheetIndex, sheetName);
				Row row = sheet.createRow(rowFrom);
				Cell cell = row.createCell(columnFrom);
				setCellValue(cell, value);
			}
		});
	}

	private void setCellValue(Cell cell, Object data) {
		if (data == null) {
			cell.setCellValue("");
			return;
		}
		if (data instanceof Date) {
			cell.setCellValue((Date) data);
			cell.setCellStyle(getDateStyle(DATE_FORMAT, cell.getRow().getSheet().getWorkbook()));
			return;
		}
		if (data instanceof Boolean) {
			cell.setCellValue(((Boolean) data).booleanValue());
			return;
		}
		if (data instanceof Number) {
			cell.setCellValue(((Number) data).doubleValue());
			return;
		}
		cell.setCellValue(data.toString());
	}

	private CellStyle getDateStyle(String dateFormat, Workbook workbook) {
		DataFormat format = workbook.createDataFormat();
		CellStyle result = workbook.createCellStyle();
		result.setDataFormat(format.getFormat("yyyy-MM-dd"));
		return result;
	}

	
	public static class Builder {
		private ExcelWriterTemplate writerTemplate;
		private int sheetIndex = -1;
		private String sheetName;
		private int rowFrom;
		private int columnFrom;
		
		public Builder templateFile(File templateFile) {
			writerTemplate.setTemplateFile(templateFile);
			return this;
		}
		
		public Builder templateFile(InputStream in, Version version) {
			writerTemplate.setTemplateFile(in, version);
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

		public Builder columnFrom(int columnFrom) {
			this.columnFrom = columnFrom;
			return this;
		}

		public Builder columnFrom(String columnFrom) {
			return columnFrom(convertColumnLabelToIndex(columnFrom));
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
		
		public ExcelWriter build() {
			if (sheetIndex < 0 && StringUtils.isBlank(sheetName)) {
				throw new IllegalArgumentException("Sheet name or index needed!");
			}
			if (rowFrom < 0) {
				throw new IllegalArgumentException("First row is less than 0!");
			}
			if (columnFrom < 0) {
				throw new IllegalArgumentException("First column is less than 0!");
			}
			return new ExcelWriter(this);
		}
	}
	
}
