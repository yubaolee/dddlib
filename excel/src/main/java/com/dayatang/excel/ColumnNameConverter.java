package com.dayatang.excel;

/**
 * 列名转换器
 * @author yyang
 *
 */
public class ColumnNameConverter {

	private ColumnNameConverter() {
	}

	/**
	 * 将列名转换为列索引，例如将列"A"转换为0
	 * @param columnName 要转换的列名
	 * @return 参数columnName代表的列的索引
	 */
	public static int convertColumnNameToIndex(String columnName) {
		if (columnName.length() > 2) {
			throw new IllegalArgumentException("Column index too large!");
		}
		String theColumn = columnName.toUpperCase();
		if (theColumn.length() == 1) {
			int letter = theColumn.charAt(0);
			return letter - 65;
		}
		int firstLetter = theColumn.charAt(0);
		int lastLetter = theColumn.charAt(1);
		return (firstLetter - 64) * 26 + lastLetter - 65;
	}

}
