package com.dayatang.cache.memcached;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.dayatang.cache.Cache;
import com.dayatang.springtest.PureSpringTestCase;

public class MemCachedBasedCacheTest extends PureSpringTestCase {

	private Cache cache;

	@Before
	public void setup() {
		super.setup();
		cache = (Cache) context.getBean("memcached");
	}

	@Test
	public void notnull() {
		assertNotNull(cache);
	}

	@Test
	public void get() {
		Object obj = cache.get("hehe");
		assertNull(obj);
	}

	@Test
	public void put() throws InterruptedException {
		try {
			Thread.sleep(1000);
			Date now = new Date();
			cache.put("time", now);
			Date obj = (Date) cache.get("time");
			assertNotNull(obj);
			assertEquals(now, obj);
		} catch (RuntimeException ex) {
			System.err.println("出错了..." + ex);
			throw ex;
		}
	}

	@Test
	public void remove() {

		assertFalse(cache.remove("no-exist"));

		Date now = new Date();
		cache.put("time", now);
		boolean delete = cache.remove("time");
		assertTrue(delete);

	}

	@Test
	public void isKeyInCache() {

		assertFalse(cache.isKeyInCache("no-exist"));

		Date now = new Date();
		cache.put("time", now);
		assertTrue(cache.isKeyInCache("time"));

	}

}
