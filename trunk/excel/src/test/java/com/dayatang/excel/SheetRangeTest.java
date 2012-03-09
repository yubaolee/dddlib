package com.dayatang.excel;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SheetRangeTest {

	private SheetRange instance;
	
	@Before
	public void setUp() throws Exception {
		instance = new SheetRange(3, 2);
		instance.addData(1,2,3,4,5);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetData() {
		List<Object[]> data = instance.getData();
		assertArrayEquals(new Object[] {1, 2}, data.get(0));
		assertArrayEquals(new Object[] {3, 4}, data.get(1));
		assertArrayEquals(new Object[] {5, null}, data.get(2));
	}

	@Test
	public void testGetRow() {
		assertArrayEquals(new Object[] {1, 2}, instance.getRow(0));
		assertArrayEquals(new Object[] {3, 4}, instance.getRow(1));
		assertArrayEquals(new Object[] {5, null}, instance.getRow(2));
	}

	@Test
	public void testGetCol() {
		assertArrayEquals(new Object[] {1, 3, 5}, instance.getCol(0));
		assertArrayEquals(new Object[] {2, 4, null}, instance.getCol(1));
	}

	@Test
	public void testGetCell() {
		assertEquals(4, instance.getCell(1, 1));
	}

}
