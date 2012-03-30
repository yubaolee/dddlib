package com.dayatang.excel;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReadingRangeTest {
	
	private ReadingRange.Builder builder;
	
	@Before
	public void setUp() throws Exception {
		builder = new ReadingRange.Builder().sheetAt(1).rowFrom(5);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void testException() {
		builder.build();
	}

	@Test
	public void testSheetIndex() {
		ReadingRange instance = builder.sheetAt(3).rowFrom(5).colRange(0, 2).build();
		assertThat(instance.getSheetIndex(), is(3));
		assertNull(instance.getSheetName());
	}

	@Test
	public void testSheetName() {
		ReadingRange instance = builder.sheetName("company").rowFrom(5).colRange(0, 2).build();
		assertThat(instance.getSheetName(), is("company"));
		assertTrue(instance.getSheetIndex() < 0);
	}

	@Test
	public void testRowFrom() {
		ReadingRange instance = builder.sheetName("company").rowFrom(8).colRange(0, 2).build();
		assertThat(instance.getRowFrom(), is(8));
	
	}
	
	@Test
	public void testRowTo() {
		ReadingRange instance = builder.rowTo(6).colRange(0, 2).build();
		assertThat(instance.getRowTo(), is(6));
	}
	
	@Test
	public void testColRange() {
		ReadingRange instance = builder.colRange(0, 2).build();
		assertArrayEquals(new int[] {0, 1, 2}, instance.getColumns());
	}

	@Test
	public void testColNameRange() {
		ReadingRange instance = builder.colRange("A", "C").build();
		assertArrayEquals(new int[] {0, 1, 2}, instance.getColumns());
	}

	@Test
	public void testColumns() {
		ReadingRange instance = builder.columns("A", "C").build();
		assertArrayEquals(new int[] {0, 2}, instance.getColumns());
	}

	@Test
	public void testColumnNames() {
		ReadingRange instance = builder.columns("A", "AA").build();
		assertArrayEquals(new int[] {0, 26}, instance.getColumns());
	}
	
}
