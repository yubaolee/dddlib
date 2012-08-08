package com.dayatang.excel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelDataset {
	private List<Object[]> data = new ArrayList<Object[]>();
	private Version version;
	private boolean isDate1904;
	
	public ExcelDataset(List<Object[]> data, Version version, boolean isDate1904) {
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
		return (Double) data.get(row)[column];
	}
	
	public Integer getInteger(int row, int column) {
		Double value = getDouble(row, column);
		return value == null ? null : value.intValue();
	}
	
	public Long getLong(int row, int column) {
		Double value = getDouble(row, column);
		return value == null ? null : value.longValue();
	}
	
	public Boolean getBoolean(int row, int column) {
		return (Boolean) data.get(row)[column];
	}
	
	public String getString(int row, int column) {
		return (String) data.get(row)[column];
	}
	
	public Date getDate(int row, int column) {
		Double value = getDouble(row, column);
		return version.getDate(value, isDate1904);
	}
}
