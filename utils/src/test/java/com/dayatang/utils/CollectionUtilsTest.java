package com.dayatang.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CollectionUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testList() {
		Collection<Item> items = new ArrayList<Item>();
		items.add(new Item(1, "A"));
		items.add(new Item(2, "B"));
		items.add(new Item(1, "C"));
		String separator = ", ";
		String result = CollectionUtils.join(items, "id", separator);
		assertEquals("1, 2, 1", result);
	}

	@Test
	public void testSet() {
		Collection<Item> items = new HashSet<Item>();
		items.add(new Item(1, "A"));
		items.add(new Item(2, "B"));
		items.add(new Item(1, "C"));
		String separator = ", ";
		String result = CollectionUtils.join(items, "id", separator);
		String[] results = result.split(separator);
		List<String> fieldList = Arrays.asList(results);
		assertEquals(2, fieldList.size());
		assertTrue(fieldList.contains("1"));
		assertTrue(fieldList.contains("2"));
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
	
	public class Item {
		private int id;
		private String name;
		public Item(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
