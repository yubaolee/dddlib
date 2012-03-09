package com.dayatang.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface ExcelReaderCallback<T> {
	T doInPoi(HSSFWorkbook workbook) throws Exception;
}
