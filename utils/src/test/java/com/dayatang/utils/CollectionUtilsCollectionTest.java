package com.dayatang.utils;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class CollectionUtilsCollectionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testJoin() {
		Collection<Item> items = new ArrayList<Item>();
		items.add(new Item(1, "A"));
		items.add(new Item(2, "B"));
		items.add(new Item(1, "C"));
		String separator = ", ";
		String result = CollectionUtils.join(items, "id", separator);
		assertEquals("1, 2, 1", result);
	}

	@Test
	public void testNull() {
		String separator = ", ";
		List<Item> items = null;
		String result = CollectionUtils.join(items, "id", separator);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testEmpty() {
		String separator = ", ";
		String result = CollectionUtils.join(new ArrayList<Object>(), "id", separator);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSingleElement() {
		Collection<Item> items = new ArrayList<Item>();
		items.add(new Item(1, "A"));
		String separator = ", ";
		String result = CollectionUtils.join(items, "name", separator);
		assertEquals("A", result);
	}
}
