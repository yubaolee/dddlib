package com.dayatang.excel;

import jxl.Workbook;

public interface ExcelCallback<T> {
	T doInJxl(Workbook workbook);
}
