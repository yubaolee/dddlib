package com.dayatang.excel;

import org.apache.commons.lang3.StringUtils;

public class ReadingRange {

	private int sheetIndex;
	private String sheetName;
	private int rowFrom, rowTo = -1;
	private int[] columns;
	private DataType[] dataTypes;
	
	public ReadingRange(Builder builder) {
		this.sheetIndex = builder.sheetIndex;
		this.sheetName = builder.sheetName;
		this.rowFrom = builder.rowFrom;
		this.rowTo = builder.rowTo;
		this.columns = builder.columns;
		this.dataTypes = builder.dataTypes;
	}
	
	public int getSheetIndex() {
		return sheetIndex;
	}

	public String getSheetName() {
		return sheetName;
	}

	public int getRowFrom() {
		return rowFrom;
	}

	public int getRowTo() {
		return rowTo;
	}

	public int[] getColumns() {
		return columns;
	}

	public DataType[] getDataTypes() {
		return dataTypes;
	}

	public static class Builder {
		private int sheetIndex = -1;
		private String sheetName;
		private int rowFrom, rowTo = -1;
		private int[] columns;
		private DataType[] dataTypes;
		
		public Builder(int sheetIndex, int rowFrom) {
			if (rowFrom < 0) {
				throw new IllegalArgumentException("First row is less than 0!");
			}
			this.rowFrom = rowFrom;
			this.sheetIndex = sheetIndex;
			sheetName = null;
		}
		
		public Builder(String sheetName, int rowFrom) {
			if (rowFrom < 0) {
				throw new IllegalArgumentException("First row is less than 0!");
			}
			this.rowFrom = rowFrom;
			this.sheetName = sheetName;
			sheetIndex = -1;
		}
		
		public Builder rowTo(int rowTo) {
			if (rowTo < rowFrom) {
				throw new IllegalArgumentException("Last row is less than first row!");
			}
			this.rowTo = rowTo;
			return this;
		}
		
		public Builder colRange(int colFrom, int colTo) {
			if (colTo < colFrom) {
				throw new IllegalArgumentException("Last column is less than first column!");
			}
			int length = colTo - colFrom + 1;
			if (length <= 0) {
				throw new IllegalArgumentException("Column range in invalid!");
			}
			columns = new int[length];
			int from = colFrom;
			for (int i = 0; i < length; i++) {
				columns[i] = from++;
			}
			return this;
		}
		
		public Builder colRange(String colFrom, String colTo) {
			return colRange(convertCol(colFrom), convertCol(colTo));
		}

		private int convertCol(String column) {
			if (column.length() > 2) {
				throw new IllegalArgumentException("Column index too large!");
			}
			String theColumn = column.toUpperCase();
			if (theColumn.length() == 1) {
				int letter = theColumn.charAt(0);
				return letter - 65;
			}
			int firstLetter = theColumn.charAt(0);
			int lastLetter = theColumn.charAt(1);
			return (firstLetter - 64) * 26 + lastLetter - 65;
		}

		public Builder columns(int... columns) {
			this.columns = columns;
			return this;
		}

		public Builder columns(String... columns) {
			this.columns = convertCols(columns);
			return this;
		}
		
		private int[] convertCols(String[] columns) {
			int[] results = new int[columns.length];
			for (int i = 0; i < columns.length; i++) {
				results[i] = convertCol(columns[i]);
			}
			return results;
		}

		public Builder colTypes(DataType... dataTypes) {
			this.dataTypes = dataTypes;
			return this;
		}
		
		public ReadingRange build() {
			if (sheetIndex < 0 && StringUtils.isEmpty(sheetName)) {
				throw new IllegalArgumentException("Sheet name or index needed!");
			}
			if (columns == null || columns.length == 0) {
				throw new IllegalArgumentException("Need one column at least!");
			}
			if (dataTypes != null && dataTypes.length != columns.length) {
				throw new IllegalArgumentException("Data type count not equals to column count!");
			}
			return new ReadingRange(this);
		}
	}
}
