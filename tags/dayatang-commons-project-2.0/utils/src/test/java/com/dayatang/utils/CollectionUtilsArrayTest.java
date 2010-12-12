package com.dayatang.utils;

import static org.junit.Assert.*;

import org.junit.*;

public class CollectionUtilsArrayTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testJoin() {
		Item[] items = {
				new Item(1, "A"),
				new Item(2, "B"),
				new Item(1, "C")
		};
		String separator = ", ";
		String result = CollectionUtils.join(items, "id", separator);
		assertEquals("1, 2, 1", result);
	}

	@Test
	public void testNull() {
		String separator = ", ";
		Item[] items = null;
		String result = CollectionUtils.join(items, "id", separator);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testEmpty() {
		Item[] items = {
		};
		String separator = ", ";
		String result = CollectionUtils.join(items, "id", separator);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSingleElement() {
		Item[] items = {
				new Item(1, "A")
		};
		String separator = ", ";
		String result = CollectionUtils.join(items, "name", separator);
		assertEquals("A", result);
	}
}
