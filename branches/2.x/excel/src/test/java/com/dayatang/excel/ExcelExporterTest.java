package com.dayatang.excel;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExcelExporterTest {

	private ExcelExporter exporter;
	
	@Before
	public void setUp() throws Exception {
		String file = getClass().getResource("/export.xls").getFile();
		exporter = new ExcelExporter(new File(file));
	}

	@After
	public void tearDown() throws Exception {
		exporter.close();
	}

	@Test
	public void testExportData() throws RowsExceededException, WriteException {
		List<Object[]> data = createData();
		exporter.createSheet("Company", "Dept", "Job", "Post", "Employee");
		exporter.exportData("Company", 0, 0, data);
		WritableWorkbook workbook = exporter.getWorkbook();
		String sn = workbook.getSheet("Company").getCell(0, 2).getContents();
		assertEquals("dayatang", sn);
	}


	private List<Object[]> createData() {
		List<Object[]> results = new ArrayList<Object[]>();
		results.add(new Object[] {"编号", "公司", "创建日期", "撤销日期", "排序号", "上级机构"});
		results.add(new Object[] {"suilink", "穗灵公司", createDate("2002-7-1"), createDate("8888-1-1"), 1, null});
		results.add(new Object[] {"dayatang", "大雅堂公司", createDate("2004-10-1"), createDate("8888-1-1"), 1, "suilink"});
		return results;
	}

	private Object createDate(String value) {
		try {
			return DateUtils.parseDate(value, new String[] {
					"yyyy-MM-dd",
					"yyyy-M-d",
					"yy-MM-dd",
					"yy-M-d"
			});
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
