package com.dayatang.domain;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class ValueTest {

	private Value value;

	@Test
	public void testValueDataTypeString() {
		value = new Value(DataType.INT, "12");
		assertEquals(DataType.INT, value.getDataType());
		assertEquals("12", value.getValue());
	}

	@Test
	public void testGetDataType() {
		value = new Value();
		value.setDataType(DataType.BIG_DECIMAL);
		assertEquals(DataType.BIG_DECIMAL, value.getDataType());
	}

	@Test
	public void testSetDataType() {
		value = new Value();
		value.setDataType(DataType.BIG_DECIMAL);
		assertEquals(DataType.BIG_DECIMAL, value.getDataType());
	}

	@Test
	public void testGetValue() {
		value = new Value();
		value.setValue("12");
		assertEquals("12", value.getValue());
	}

	@Test
	public void testSetValue() {
		value = new Value();
		value.setValue("12");
		assertEquals("12", value.getValue());
	}

	@Test
	public void testGetString() {
		value = new Value(DataType.STRING, "12");
		assertEquals("12", value.getString());
		value = new Value(DataType.STRING, null);
		assertNull(value.getString());
	}

	@Test
	public void testGetInt() {
		value = new Value(DataType.INT, "12");
		assertEquals(12, value.getInt());
		value = new Value(DataType.INT, null);
		assertEquals(0, value.getInt());
	}

	@Test
	public void testGetDouble() {
		value = new Value(DataType.DOUBLE, "12");
		assertEquals(12.0, value.getDouble(), 0.0001);
		value = new Value(DataType.DOUBLE, null);
		assertEquals(0.0, value.getDouble(), 0.0001);
	}

	@Test
	public void testGetBigDecimal() {
		value = new Value(DataType.BIG_DECIMAL, "12");
		assertEquals(new BigDecimal("12"), value.getBigDecimal());
		value = new Value(DataType.BIG_DECIMAL, null);
		assertNull(value.getBigDecimal());
	}

	@Test
	public void testGetBoolean() {
		value = new Value(DataType.BOOLEAN, "true");
		assertTrue(value.getBoolean());
		value = new Value(DataType.BOOLEAN, "false");
		assertFalse(value.getBoolean());
		value = new Value(DataType.BOOLEAN, null);
		assertFalse(value.getBoolean());
	}

	@Test
	public void testGetDate() {
		value = new Value(DataType.DATE, "2000-1-5");
		assertEquals(createDate(2000, 1, 5), value.getDate());
		value = new Value(DataType.DATE, "2000-01-05");
		assertEquals(createDate(2000, 1, 5), value.getDate());
		
		value = new Value(DataType.TIME, "3:5:12");
		assertEquals(createTime(3, 5, 12), value.getDate());
		value = new Value(DataType.TIME, "03:05:12");
		assertEquals(createTime(3, 5, 12), value.getDate());
		
		value = new Value(DataType.DATE_TIME, "2000-1-5 03:05:12");
		assertEquals(createDateTime(2000, 1, 5, 3, 5, 12), value.getDate());
		
		value = new Value(DataType.DATE_TIME, null);
		assertNull(value.getDate());
	}

	private Date createDate(int year, int month, int date) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, date, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	private Date createTime(int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(1970, 0, 1, hour, minute, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	private Date createDateTime(int year, int month, int date, int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, date, hour, minute, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	@Test
	public void testEqualsObject() {
		value = new Value(DataType.DATE, "2000-1-5");
		Value value1 = new Value(DataType.DATE, "2000-1-5");
		assertEquals(value1, value);

		value1 = new Value(DataType.DATE, "2000-1-5");
		assertEquals(value1, value);
		
		value1 = new Value(DataType.DATE, "2000-2-5");
		assertFalse(value1.equals(value));

		value1 = new Value(DataType.TIME, "2000-1-5");
		assertFalse(value1.equals(value));

		value1 = new Value(null, "2000-1-5");
		assertFalse(value1.equals(value));
	}

	@Test
	public void testToString() {
		value = new Value(DataType.DATE, "2000-1-5");
		assertEquals("2000-1-5", value.toString());
	}

}
