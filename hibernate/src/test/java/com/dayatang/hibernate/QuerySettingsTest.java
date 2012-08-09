/**
 *
 */
package com.dayatang.hibernate;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dayatang.commons.domain.Dictionary;
import com.dayatang.commons.domain.DictionaryCategory;
import com.dayatang.commons.repository.BtmUtils;
import com.dayatang.commons.repository.HibernateUtils;
import com.dayatang.domain.AggregateRootEntity;
import com.dayatang.domain.Criterions;
import com.dayatang.domain.QueryException;
import com.dayatang.domain.QuerySettings;

/**
 * 
 * @author yang
 */
public class QuerySettingsTest {

	private static SessionFactory sessionFactory;
	
	private Session session;
	
	Transaction tx;

	private EntityRepositoryHibernate repository;
	
	private QuerySettings<Dictionary> settings;

	private DictionaryCategory gender;

	private DictionaryCategory education;

	private Dictionary male;

	private Dictionary female;

	private Dictionary undergraduate;


	@BeforeClass
	public static void classSetUp() throws Exception {
		BtmUtils.setupDataSource();
		sessionFactory = HibernateUtils.getSessionFactory();
	}
	
	@AfterClass
	public static void classTearDown() throws Exception {
		sessionFactory.close();
		BtmUtils.closeDataSource();
	}
	
	
	@Before
	public void setUp() {
		session = sessionFactory.openSession();
		
		tx = session.beginTransaction();
		repository = new EntityRepositoryHibernate(session);
		AggregateRootEntity.setRepository(repository);
		settings = QuerySettings.create(Dictionary.class);
		gender = createCategory("gender", 1);
		education = createCategory("education", 2);
		male = createDictionary("01", "男", gender, 100, "01");
		female = createDictionary("02", "女", gender, 150, "01");
		undergraduate = createDictionary("01", "本科", education, 200, "05");
	}

	@After
	public void tearDown() {
		tx.rollback();
		if (session != null && session.isOpen()) {
			session.close();
		}
		AggregateRootEntity.setRepository(null);
	}

	@Test
	public void testEq() {
		settings.eq("category", gender);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testNotEq() {
		settings.notEq("category", gender);
		List<Dictionary> results = repository.find(settings);
		Dictionary dictionary = results.get(0);
		assertEquals(education, dictionary.getCategory());
	}

	@Test
	public void testGe() {
		settings.ge("sortOrder", 150);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testGt() {
		settings.gt("sortOrder", 150);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testLe() {
		settings.le("sortOrder", 150);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testLt() {
		settings.lt("sortOrder", 150);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertFalse(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testEqProp() {
		settings.eqProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertFalse(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testNotEqProp() {
		settings.notEqProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testGtProp() {
		settings.gtProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testGeProp() {
		settings.geProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testLtProp() {
		settings.ltProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testLeProp() {
		settings.leProp("code", "parentCode");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testSizeEq() {
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeEq("dictionaries", 2);
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(gender));
		assertFalse(results.contains(education));
	}

	@Test
	public void testSizeNotEq() {
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeNotEq("dictionaries", 2);
		List<DictionaryCategory> results = repository.find(settings);
		assertFalse(results.contains(gender));
		assertTrue(results.contains(education));
	}

	@Test
	public void testSizeGt() {
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeGt("dictionaries", 1);
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(gender));
		assertFalse(results.contains(education));
	}

	@Test
	public void testSizeGe() {
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeGe("dictionaries", 2);
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(gender));
		assertFalse(results.contains(education));
	}

	@Test
	public void testSizeLt() {
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeLt("dictionaries", 2);
		List<DictionaryCategory> results = repository.find(settings);
		assertFalse(results.contains(gender));
		assertTrue(results.contains(education));
	}

	@Test
	public void testSizeLe() {
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.sizeLe("dictionaries", 2);
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(gender));
		assertTrue(results.contains(education));
	}

	@Test
	public void testIsEmpty() {
		DictionaryCategory empty = createCategory("a", 3);
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.isEmpty("dictionaries");
		List<DictionaryCategory> results = repository.find(settings);
		assertTrue(results.contains(empty));
		assertFalse(results.contains(gender));
		assertFalse(results.contains(education));
	}

	@Test
	public void testNotEmpty() {
		DictionaryCategory empty = createCategory("a", 3);
		QuerySettings<DictionaryCategory> settings = QuerySettings.create(DictionaryCategory.class);
		settings.notEmpty("dictionaries");
		List<DictionaryCategory> results = repository.find(settings);
		assertFalse(results.contains(empty));
		assertTrue(results.contains(gender));
		assertTrue(results.contains(education));
	}

	@Test
	public void testContainsText() {
		settings.containsText("text", "科");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(undergraduate));
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
	}

	@Test
	public void testStartsWithText() {
		settings.startsWithText("text", "本");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(undergraduate));
		
		settings = QuerySettings.create(Dictionary.class).startsWithText("text", "科");
		results = repository.find(settings);
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testInEntity() {
		Set<DictionaryCategory> params = new HashSet<DictionaryCategory>();
		params.add(education);
		params.add(gender);
		settings.in("category", params);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testInString() {
		Set<String> params = new HashSet<String>();
		params.add("男");
		params.add("女");
		settings.in("text", params);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@SuppressWarnings("unchecked")
	@Test(expected = QueryException.class)
	public void testInEmpty() {
		settings.in("id", Collections.EMPTY_LIST);
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.isEmpty());
	}

	@Test
	public void testNotInEntity() {
		Set<Long> params = new HashSet<Long>();
		params.add(male.getId());
		params.add(female.getId());
		settings.notIn("id", params);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testNotInString() {
		Set<String> params = new HashSet<String>();
		params.add("男");
		params.add("女");
		settings.notIn("text", params);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertFalse(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@SuppressWarnings("unchecked")
	@Test(expected = QueryException.class)
	public void testNotInEmpty() {
		settings.notIn("id", Collections.EMPTY_LIST);
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.isEmpty());
	}

	@Test
	public void testIsNull() {
		settings.isNull("description");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testNotNull() {
		settings.notNull("text");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertTrue(results.contains(undergraduate));
	}

	@Test
	public void testBetween() {
		settings.between("parentCode", "01", "02");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testAnd() {
		settings.and(Criterions.eq("code", "01"), Criterions.eq("category", gender));
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertFalse(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testOr() {
		settings.or(Criterions.eq("text", "男"), Criterions.eq("sortOrder", 150));
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	//@Test
	public void testNot() {
		settings.not(Criterions.eq("code", "01"));
		List<Dictionary> results = repository.find(settings);
		assertFalse(results.contains(male));
		assertTrue(results.contains(female));
		assertFalse(results.contains(undergraduate));
	}

	@Test
	public void testFindPaging() {
		createDictionary("08", "xyz", education, 150, "01");
		createDictionary("09", "xyy", education, 160, "02");
		settings.setFirstResult(1).setMaxResults(2);
		List<Dictionary> results = repository.find(settings);
		assertEquals(2, results.size());
	}

	@Test
	public void testFindOrder() {
		settings.asc("sortOrder");
		List<Dictionary> results = repository.find(settings);
		assertTrue(results.indexOf(male) < results.indexOf(female));
		assertTrue(results.indexOf(female) < results.indexOf(undergraduate));

		settings = QuerySettings.create(Dictionary.class).desc("sortOrder");
		results = repository.find(settings);
		assertTrue(results.indexOf(male) > results.indexOf(female));
		assertTrue(results.indexOf(female) > results.indexOf(undergraduate));
	}

	@Test
	public void testAlias() {
		List<Dictionary> results = repository.find(QuerySettings.create(Dictionary.class).eq("category.name", "education"));
		assertTrue(results.contains(undergraduate));
	}

	private DictionaryCategory createCategory(String name, int sortOrder) {
		DictionaryCategory category = new DictionaryCategory();
		category.setName(name);
		category.setSortOrder(sortOrder);
		session.persist(category);
		return category;
	}

	private Dictionary createDictionary(String code, String text, DictionaryCategory category, int sortOrder,
			String parentCode) {
		Dictionary dictionary = new Dictionary(code, text, category);
		dictionary.setSortOrder(sortOrder);
		dictionary.setParentCode(parentCode);
		session.persist(dictionary);
		return dictionary;
	}
}
