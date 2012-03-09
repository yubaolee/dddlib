package com.dayatang.excel;

import jxl.Workbook;

public interface ExcelReaderCallback<T> {
	T doInJxl(Workbook workbook);
}
