package com.dayatang.excel;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReadingRangeTest {
	
	private ReadingRange.Builder builder;
	private String[] columnsLabels;
	private DataType[] columnTypes;
			
	@Before
	public void setUp() throws Exception {
		columnsLabels = new String[] {"A", "B", "C"};
		columnTypes = new DataType[] {DataType.STRING, DataType.DATE, DataType.NUMERIC};
		builder = new ReadingRange.Builder().sheetAt(1).rowFrom(5).columns(columnsLabels, columnTypes);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSheetIndex() {
		ReadingRange instance = builder.sheetAt(3).build();
		assertThat(instance.getSheetIndex(), is(3));
		assertNull(instance.getSheetName());
	}

	@Test
	public void testSheetName() {
		ReadingRange instance = builder.sheetName("company").build();
		assertThat(instance.getSheetName(), is("company"));
		assertTrue(instance.getSheetIndex() < 0);
	}

	@Test
	public void testRowFrom() {
		ReadingRange instance = builder.rowFrom(8).build();
		assertThat(instance.getRowFrom(), is(8));
	
	}
	
	@Test
	public void testRowTo() {
		ReadingRange instance = builder.rowTo(6).build();
		assertThat(instance.getRowTo(), is(6));
	}

	@Test
	public void testColumnsByIndex() {
		int[] columnIndexes = new int[] {0, 2};
		columnTypes = new DataType[] {DataType.STRING, DataType.NUMERIC};
		ReadingRange instance = builder.columns(columnIndexes, columnTypes).build();
		assertArrayEquals(columnIndexes, instance.getColumnIndexes());
		assertArrayEquals(columnTypes, instance.getColumnTypes());
	}

	@Test
	public void testColumnNames() {
		columnsLabels = new String[] {"A", "C"};
		columnTypes = new DataType[] {DataType.STRING, DataType.NUMERIC};
		ReadingRange instance = builder.columns(columnsLabels, columnTypes).build();
		assertArrayEquals(new int[] {0, 2}, instance.getColumnIndexes());
		assertArrayEquals(columnTypes, instance.getColumnTypes());
	}
	
}
