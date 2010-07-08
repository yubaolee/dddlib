package com.dayatang.domain;


import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.ParseException;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataTypeTest {
	
	private final String[] DATE_FORMAT = new String[] {
		"yyyy-MM-dd",
		"hh:mm:ss",
		"yyyy-MM-dd hh:mm:ss"
	};

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetDefaultValue() throws ParseException {
		assertEquals("", DataType.STRING.getDefaultValue());
		assertEquals(0, DataType.INT.getDefaultValue());
		assertEquals(0.0, DataType.DOUBLE.getDefaultValue());
		assertEquals(BigDecimal.ZERO, DataType.BIG_DECIMAL.getDefaultValue());
		assertEquals(false, DataType.BOOLEAN.getDefaultValue());
		assertEquals(DateUtils.parseDate("1970-01-01", DATE_FORMAT), DataType.DATE.getDefaultValue());
		assertEquals(DateUtils.parseDate("00:00:00", DATE_FORMAT), DataType.TIME.getDefaultValue());
		assertEquals(DateUtils.parseDate("1970-01-01 00:00:00", DATE_FORMAT), DataType.DATE_TIME.getDefaultValue());
	}
}
