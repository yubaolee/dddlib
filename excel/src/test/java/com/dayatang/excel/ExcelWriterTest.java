package com.dayatang.excel;

import static org.junit.Assert.*;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

public class ExcelWriterTest {

	private File file;
	private ExcelWriter instance;
	
	@Before
	public void setUp() throws Exception {
		String outFileName = getClass().getResource("/export.xls").getFile();
		file = new File(outFileName);
		instance = new ExcelWriter(file);
	}

	@Test
	public void testExportData() throws Exception {
		List<Object[]> data = createData();
		instance.write("Company", 0, 0, data);
		ExcelReader reader = new ExcelReader(file);
		String sn = reader.readCellValue("Company", 2, 0).toString();
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
