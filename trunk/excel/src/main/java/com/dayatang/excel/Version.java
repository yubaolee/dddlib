package com.dayatang.excel;

public enum Version {
	XLS,
	XLSX;
	
	public static Version as(String filename) {
		return filename.toLowerCase().endsWith("xslx") ? XLSX : XLS;
	}
}
