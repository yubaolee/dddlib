package com.dayatang.excel;

public class ExcelRange {
	private int rowFrom;
	private int rowTo = -1;
	private int columnFrom;
	private int columnTo;
	 
	public int getRowFrom() {
		return rowFrom;
	}

	public int getRowTo() {
		return rowTo;
	}

	public int getColumnFrom() {
		return columnFrom;
	}

	public int getColumnTo() {
		return columnTo;
	}

	public ExcelRange rowFrom(int rowFrom) {
		this.rowFrom = rowFrom;
		return this;
	}
	
	public ExcelRange rowTo(int rowTo) {
		this.rowTo = rowTo;
		return this;
	}
	
	public ExcelRange rowRange(int rowFrom, int rowTo) {
		if (rowTo < rowFrom) {
			throw new IllegalArgumentException("Last row is less than first row!");
		}
		this.rowFrom = rowFrom;
		this.rowTo = rowTo;
		return this;
		
	}

	public ExcelRange columnRange(int columnFrom, int columnTo) {
		if (columnTo < columnFrom) {
			throw new IllegalArgumentException("Last column is less than first column!");
		}
		this.columnFrom = columnFrom;
		this.columnTo = columnTo;
		return this;
	}

	public ExcelRange columnRange(String columnFrom, String columnTo) {
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
}