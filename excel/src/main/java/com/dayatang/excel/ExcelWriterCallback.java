package com.dayatang.excel;

import jxl.write.WritableWorkbook;

public interface ExcelWriterCallback {
	void doInJxl(WritableWorkbook workbook) throws Exception;
}
