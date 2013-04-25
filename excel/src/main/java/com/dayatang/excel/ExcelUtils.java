package com.dayatang.excel;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * Excel工具类
 * @author yyang
 *
 */
public class ExcelUtils {

	private ExcelUtils() {
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
	
	public static Double getDouble(Object data) {
		if (data == null) {
			return null;
		}
		if (! (data instanceof Double)) {
			throw new IllegalStateException("数据类型错误：单元格中的数据不是数值类型");
		}
		return (Double) data;
	}
	
	public static Integer getInt(Object data) {
		Double value = getDouble(data);
		return value == null ? null : value.intValue();
	}
	
	public static Long getLong(Object data) {
		Double value = getDouble(data);
		return value == null ? null : value.longValue();
	}
	
	public static Boolean getBoolean(Object data) {
		if (data == null) {
			return null;
		}
		if (! (data instanceof Boolean)) {
			throw new IllegalStateException("数据类型错误：单元格中的数据不是布尔类型");
		}
		return (Boolean) data;
	}
	
	public static String getString(Object data) {
		if (data == null) {
			return null;
		}
		if (StringUtils.isBlank(data.toString())) {
			return null;
		}
		return data.toString();
	}
	
	public static Date getDate(Object data, Version version, boolean isDate1904) {
		Double value = getDouble(data);
		return version.getDate(value, isDate1904);
	}
	
}
