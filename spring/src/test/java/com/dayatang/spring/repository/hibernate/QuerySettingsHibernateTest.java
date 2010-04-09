/**
 *
 */
package com.dayatang.spring.repository.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.QuerySettings;
import com.dayatang.spring.domain.Dictionary;
import com.dayatang.spring.domain.DictionaryCategory;
import com.dayatang.spring.repository.EntityRepositoryHibernate;

/**
 * 
 * @author yang
 */
public class QuerySettingsHibernateTest {

	private static EntityRepositoryHibernate repository;

	private Transaction tx;
	
	private QuerySettings<Dictionary> settings;
	
	private DictionaryCategory gender;

	@Before
	public void setUp() {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		tx = sessionFactory.getCurrentSession().beginTransaction();
		repository = new EntityRepositoryHibernate(sessionFactory);
		AbstractEntity.setRepository(repository);
		settings = QuerySettings.create(Dictionary.class);
		gender = DictionaryCategory.getByName(DictionaryCategory.GENDER);
	}

	@After
	public void tearDown() {
		tx.commit();
		HibernateUtils.close();
	}

	@Test
	public void testEq() {
		settings.eq("category", gender);
		List<Dictionary> results = repository.find(settings);
		for (Dictionary dictionary : results) {
			assertEquals(gender, dictionary.getCategory());
		}
	}

	@Test
	public void testNotEq() {
		settings.notEq("category", gender);
		List<Dictionary> results = repository.find(settings);
		for (Dictionary dictionary : results) {
			assertFalse(gender.equals(dictionary.getCategory()));
		}
	}

	@Test
	public void testGe() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		settings.ge("id", 5L);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(dictionary4));
		assertTrue(results.contains(dictionary5));
		assertTrue(results.contains(dictionary6));
	}

	@Test
	public void testGt() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		settings.gt("id", 5L);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(dictionary4));
		assertFalse(results.contains(dictionary5));
		assertTrue(results.contains(dictionary6));
	}

	@Test
	public void testLe() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		settings.le("id", 5L);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(dictionary4));
		assertTrue(results.contains(dictionary5));
		assertFalse(results.contains(dictionary6));
	}

	@Test
	public void testLt() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		settings.lt("id", 5L);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(dictionary4));
		assertFalse(results.contains(dictionary5));
		assertFalse(results.contains(dictionary6));
	}

	@Test
	public void testEqProp() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		settings.eqProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(dictionary4));
		assertFalse(results.contains(dictionary5));
		assertFalse(results.contains(dictionary6));
	}

	@Test
	public void testNotEqProp() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		settings.notEqProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(dictionary4));
		assertTrue(results.contains(dictionary5));
		assertTrue(results.contains(dictionary6));
	}

	@Test
	public void testGtProp() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		settings.gtProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(dictionary4));
		assertTrue(results.contains(dictionary5));
		assertTrue(results.contains(dictionary6));
	}

	@Test
	public void testGeProp() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		settings.geProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(dictionary4));
		assertTrue(results.contains(dictionary5));
		assertTrue(results.contains(dictionary6));
	}

	@Test
	public void testLtProp() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		settings.ltProp("parentCode", "code");
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(dictionary4));
		assertTrue(results.contains(dictionary5));
		assertTrue(results.contains(dictionary6));
	}

	@Test
	public void testLeProp() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		settings.leProp("parentCode", "code");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(dictionary4));
		assertTrue(results.contains(dictionary5));
		assertTrue(results.contains(dictionary6));
	}

	@Test
	public void testSizeEq() {
		DictionaryCategory category1 = repository.get(DictionaryCategory.class, 1L);
		DictionaryCategory category2 = repository.get(DictionaryCategory.class, 2L);
		DictionaryCategory category3 = repository.get(DictionaryCategory.class, 3L);
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeEq("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(category1));
		assertFalse(results.contains(category2));
		assertFalse(results.contains(category3));
	}

	@Test
	public void testSizeNotEq() {
		DictionaryCategory category1 = repository.get(DictionaryCategory.class, 1L);
		DictionaryCategory category2 = repository.get(DictionaryCategory.class, 2L);
		DictionaryCategory category3 = repository.get(DictionaryCategory.class, 3L);
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeNotEq("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(settings);
		assertFalse(results.contains(category1));
		assertTrue(results.contains(category2));
		assertTrue(results.contains(category3));
	}
	
	@Test
	public void testSizeGt() {
		DictionaryCategory category1 = repository.get(DictionaryCategory.class, 1L);
		DictionaryCategory category2 = repository.get(DictionaryCategory.class, 2L);
		DictionaryCategory category3 = repository.get(DictionaryCategory.class, 3L);
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeGt("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(settings);
		assertFalse(results.contains(category1));
		assertTrue(results.contains(category2));
		assertTrue(results.contains(category3));
	}
	
	@Test
	public void testSizeGe() {
		DictionaryCategory category1 = repository.get(DictionaryCategory.class, 1L);
		DictionaryCategory category2 = repository.get(DictionaryCategory.class, 2L);
		DictionaryCategory category3 = repository.get(DictionaryCategory.class, 3L);
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeGe("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(category1));
		assertTrue(results.contains(category2));
		assertTrue(results.contains(category3));
	}
	
	@Test
	public void testSizeLt() {
		DictionaryCategory category1 = repository.get(DictionaryCategory.class, 1L);
		DictionaryCategory category2 = repository.get(DictionaryCategory.class, 2L);
		DictionaryCategory category3 = repository.get(DictionaryCategory.class, 3L);
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeLt("dictionaries", 4);
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(category1));
		assertFalse(results.contains(category2));
		assertFalse(results.contains(category3));
	}
	
	@Test
	public void testSizeLe() {
		DictionaryCategory category1 = repository.get(DictionaryCategory.class, 1L);
		DictionaryCategory category2 = repository.get(DictionaryCategory.class, 2L);
		DictionaryCategory category3 = repository.get(DictionaryCategory.class, 3L);
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeLe("dictionaries", 3);
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(category1));
		assertFalse(results.contains(category2));
		assertFalse(results.contains(category3));
	}

	@Test 
	public void testIsEmpty() {
		DictionaryCategory category1 = repository.get(DictionaryCategory.class, 1L);
		DictionaryCategory category9 = repository.get(DictionaryCategory.class, 9L);
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.isEmpty("dictionaries");
		List<DictionaryCategory> results = repository.find(settings);
		assertFalse(results.contains(category1));
		assertTrue(results.contains(category9));
	}

	@Test 
	public void testNotEmpty() {
		DictionaryCategory category1 = repository.get(DictionaryCategory.class, 1L);
		DictionaryCategory category9 = repository.get(DictionaryCategory.class, 9L);
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.notEmpty("dictionaries");
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(category1));
		assertFalse(results.contains(category9));
	}
	
	@Test
	public void testContainsText() {
		Dictionary dictionary7 = repository.get(Dictionary.class, 7L);
		Dictionary dictionary9 = repository.get(Dictionary.class, 9L);
		Dictionary dictionary11 = repository.get(Dictionary.class, 11L);
		settings.containsText("text", "大学");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(dictionary7));
		assertTrue(results.contains(dictionary9));
		assertFalse(results.contains(dictionary11));
	}

	@Test
	public void testStartsWithText() {
		Dictionary dictionary7 = repository.get(Dictionary.class, 7L);
		Dictionary dictionary9 = repository.get(Dictionary.class, 9L);
		Dictionary dictionary11 = repository.get(Dictionary.class, 11L);
		settings.startsWithText("text", "大学");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(dictionary7));
		assertFalse(results.contains(dictionary9));
		assertFalse(results.contains(dictionary11));
	}

	@Test
	public void testInEntity() {
		Dictionary dictionary7 = repository.get(Dictionary.class, 7L);
		Dictionary dictionary9 = repository.get(Dictionary.class, 9L);
		Dictionary dictionary11 = repository.get(Dictionary.class, 11L);
		Set<Long> params = new HashSet<Long>();
		params.add(7L);
		params.add(9L);
		settings.in("id", params);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(dictionary7));
		assertTrue(results.contains(dictionary9));
		assertFalse(results.contains(dictionary11));
	}

	@Test
	public void testInString() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		Set<String> params = new HashSet<String>();
		params.add("研究生");
		params.add("研究生毕业");
		settings.in("text", params);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(dictionary4));
		assertTrue(results.contains(dictionary5));
		assertFalse(results.contains(dictionary6));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testInEmpty() {
		settings.in("id", Collections.EMPTY_LIST);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.isEmpty());
	}

	@Test
	public void testIsNull() {
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		Dictionary dictionary15 = repository.get(Dictionary.class, 15L);
		settings.isNull("description");
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(dictionary5));
		assertTrue(results.contains(dictionary6));
		assertFalse(results.contains(dictionary15));
	}

	@Test
	public void testNotNull() {
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		Dictionary dictionary15 = repository.get(Dictionary.class, 15L);
		settings.notNull("description");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(dictionary5));
		assertFalse(results.contains(dictionary6));
		assertTrue(results.contains(dictionary15));
	}

	@Test
	public void testBetween() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		QuerySettings<Dictionary> settings = QuerySettings.create(Dictionary.class).between("id", 5L, 9L);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(dictionary4));
		assertTrue(results.contains(dictionary6));
	}

	@Test
	public void testFindPaging() {
		QuerySettings<Dictionary> settings = QuerySettings.create(Dictionary.class).eq("category", DictionaryCategory.getByName(DictionaryCategory.EDUCATION))
				.setFirstResult(2).setMaxResults(10).asc("sortOrder");
		List<Dictionary> results = repository.find(settings);
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 3L);
		Dictionary dictionary6 = repository.get(Dictionary.class, 6L);
		assertEquals(10, results.size());
		assertFalse(results.contains(dictionary4));
		assertFalse(results.contains(dictionary5));
		assertTrue(results.contains(dictionary6));
	}

	@Test
	public void testFindOrder() {
		Dictionary dictionary4 = repository.get(Dictionary.class, 4L);
		Dictionary dictionary5 = repository.get(Dictionary.class, 5L);

		QuerySettings<Dictionary> settings = QuerySettings.create(Dictionary.class).eq("category", DictionaryCategory.getByName(DictionaryCategory.EDUCATION))
				.asc("sortOrder");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.indexOf(dictionary4) < results.indexOf(dictionary5));

		settings = QuerySettings.create(Dictionary.class).eq("category", DictionaryCategory.getByName(DictionaryCategory.EDUCATION)).desc("sortOrder");
		results = repository.find(settings);
		assertTrue(results.indexOf(dictionary4) > results.indexOf(dictionary5));
	}

	@Test
	public void testAlias() {
		String education = DictionaryCategory.EDUCATION;
		List<Dictionary> results = repository.find(QuerySettings.create(Dictionary.class).eq("category.name", education));
		Dictionary graduate = Dictionary.get(4L);
		assertTrue(results.contains(graduate));
		Dictionary doctor = Dictionary.get(46L);
		assertFalse(results.contains(doctor));
	}
}
