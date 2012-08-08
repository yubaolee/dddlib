package com.dayatang.excel;

import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.DateUtil;

public enum Version {
	XLS {
		@Override
		public Date getDate(double value, boolean isDate1904) {
			return HSSFDateUtil.getJavaDate(value, isDate1904);
		}
	},
	XLSX {
		@Override
		public Date getDate(double value, boolean isDate1904) {
			return DateUtil.getJavaDate(value, isDate1904);
		}
	};

	public static Version of(String filename) {
		return filename.toLowerCase().endsWith("xslx") ? XLSX : XLS;
	}

	public abstract Date getDate(double value, boolean isDate1904);
}
