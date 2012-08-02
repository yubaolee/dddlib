package com.dayatang.excel;

import org.apache.commons.lang3.StringUtils;

public class ReadingRange {

	private int sheetIndex;
	private String sheetName;
	private int rowFrom, rowTo = -1;
	private int[] columnIndexes;
	private DataType[] columnTypes;
	
	public ReadingRange(Builder builder) {
		this.sheetIndex = builder.sheetIndex;
		this.sheetName = builder.sheetName;
		this.rowFrom = builder.rowFrom;
		this.rowTo = builder.rowTo;
		this.columnIndexes = builder.columnIndexes;
		this.columnTypes = builder.columnTypes;
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

	public int[] getColumnIndexes() {
		return columnIndexes;
	}

	public DataType[] getColumnTypes() {
		return columnTypes;
	}

	public static class Builder {
		private int sheetIndex = -1;
		private String sheetName;
		private int rowFrom = -1, rowTo = -1;
		private int[] columnIndexes;
		private DataType[] columnTypes;

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

		public Builder columns(int[] columnIndexes, DataType[] columnTypes) {
			if (columnIndexes == null || columnTypes == null) {
				throw new IllegalArgumentException("The columns and types can not be null!");
			}
			if (columnIndexes.length != columnTypes.length) {
				throw new IllegalArgumentException("The count of columns and types is not equals!");
			}
			this.columnIndexes = columnIndexes;
			this.columnTypes = columnTypes;
			return this;
		}

		public Builder columns(String[] columnLabels, DataType[] columnTypes) {
			return columns(convertColumnLabelToIndex(columnLabels), columnTypes);
		}

		public Builder columns(int columnFrom, int columnTo, DataType[] columnTypes) {
			if (columnTo < columnFrom) {
				throw new IllegalArgumentException("Last column is less than first column!");
			}
			this.columnIndexes = getColumnIndexes(columnFrom, columnTo);
			if (columnIndexes.length != columnTypes.length) {
				throw new IllegalArgumentException("The count of columns and types is not equals!");
			}
			this.columnTypes = columnTypes;
			return this;
		}

		private int[] getColumnIndexes(int columnFrom, int columnTo) {
			int[] results = new int[columnTo - columnFrom + 1];
			for (int i = 0; i < results.length; i++) {
				results[i] = columnFrom + i;
			}
			return results;
		}

		public Builder columns(String columnFrom, String columnTo, DataType[] columnTypes) {
			return columns(convertColumnLabelToIndex(columnFrom), convertColumnLabelToIndex(columnTo), columnTypes);
		}
		
		private int[] convertColumnLabelToIndex(String[] columnLabels) {
			int[] results = new int[columnLabels.length];
			for (int i = 0; i < columnLabels.length; i++) {
				results[i] = convertColumnLabelToIndex(columnLabels[i]);
			}
			return results;
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
		
		public ReadingRange build() {
			if (sheetIndex < 0 && StringUtils.isEmpty(sheetName)) {
				throw new IllegalArgumentException("Sheet name or index needed!");
			}
			if (rowFrom < 0) {
				throw new IllegalArgumentException("First row is less than 0!");
			}
			if (rowTo >= 0 && rowTo < rowFrom) {
				throw new IllegalArgumentException("Last row is less than first row!");
			}
			if (columnIndexes == null || columnIndexes.length == 0) {
				throw new IllegalArgumentException("Need one column at least!");
			}
			return new ReadingRange(this);
		}
	}
}
