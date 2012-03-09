package com.dayatang.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface ExcelWriterCallback {
	void doInPoi(HSSFWorkbook workbook) throws Exception;
}
