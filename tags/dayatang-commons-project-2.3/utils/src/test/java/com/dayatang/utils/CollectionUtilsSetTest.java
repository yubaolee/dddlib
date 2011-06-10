package com.dayatang.utils;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class CollectionUtilsSetTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testJoin() {
		Collection<Item> items = new HashSet<Item>();
		items.add(new Item(1, "A"));
		items.add(new Item(2, "B"));
		items.add(new Item(1, "C"));
		String separator = ", ";
		String result = CollectionUtils.join(items, "id", separator);
		List<String> fieldList = toList(result, separator);
		assertEquals(2, fieldList.size());
		assertTrue(fieldList.contains("1"));
		assertTrue(fieldList.contains("2"));
	}

	private List<String> toList(String value, String separator) {
		String[] results = value.split(separator);
		return Arrays.asList(results);
	}

	@Test
	public void testNull() {
		String separator = ", ";
		Set<Item> items = null;
		String result = CollectionUtils.join(items, "id", separator);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testEmpty() {
		String separator = ", ";
		String result = CollectionUtils.join(new HashSet<Object>(), "id", separator);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSingleElement() {
		Collection<Item> items = new HashSet<Item>();
		items.add(new Item(1, "A"));
		String separator = ", ";
		String result = CollectionUtils.join(items, "name", separator);
		assertEquals("A", result);
	}

}
