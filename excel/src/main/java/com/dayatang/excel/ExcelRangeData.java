package com.dayatang.excel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ExcelRangeData {
	private List<Object[]> data = new ArrayList<Object[]>();
	private Version version;
	private boolean isDate1904;
	
	public ExcelRangeData(List<Object[]> data, Version version, boolean isDate1904) {
		super();
		this.data = data;
		this.version = version;
		this.isDate1904 = isDate1904;
	}
	
	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return data.isEmpty() ? 0 : data.get(0).length;
	}
	
	public Double getDouble(int row, int column) {
		Object value = data.get(row)[column];
		if (value == null) {
			return null;
		}
		if (! (value instanceof Double)) {
			throw new IllegalStateException("数据类型错误：单元格中的数据不是数值/日期类型");
		}
		return (Double) value;
	}
	
	public Integer getInt(int row, int column) {
		Double value = getDouble(row, column);
		return value == null ? null : value.intValue();
	}
	
	public Long getLong(int row, int column) {
		Double value = getDouble(row, column);
		return value == null ? null : value.longValue();
	}
	
	public Boolean getBoolean(int row, int column) {
		Object value = data.get(row)[column];
		if (value == null) {
			return null;
		}
		if (! (value instanceof Boolean)) {
			throw new IllegalStateException("数据类型错误：单元格中的数据不是布尔类型");
		}
		return (Boolean) value;
	}
	
	public String getString(int row, int column) {
		Object value = data.get(row)[column];
		if (value == null) {
			return null;
		}
		if (StringUtils.isBlank(value.toString())) {
			return null;
		}
		return value.toString();
	}
	
	public Date getDate(int row, int column) {
		Double value = getDouble(row, column);
		return version.getDate(value, isDate1904);
	}
}
