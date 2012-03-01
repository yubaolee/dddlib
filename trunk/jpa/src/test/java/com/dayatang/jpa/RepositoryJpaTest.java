/**
 *
 */
package com.dayatang.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.validation.ValidationException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dayatang.commons.domain.Dictionary;
import com.dayatang.commons.domain.DictionaryCategory;
import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.ExampleSettings;
import com.dayatang.domain.QuerySettings;

/**
 * 
 * @author yang
 */
public class RepositoryJpaTest {

	private static EntityManagerFactory emf;
	
	private EntityManager entityManager;
	
	private EntityRepositoryJpa repository;
	
	private EntityTransaction tx;

	@BeforeClass
	public static void setUpClass() {
		emf = Persistence.createEntityManagerFactory("default");
	}
	
	@AfterClass
	public static void tearDownClass() {
		emf.close();
	}
	
	@Before
	public void setUp() {
		entityManager = emf.createEntityManager();
		tx = entityManager.getTransaction();
		tx.begin();
		repository = new EntityRepositoryJpa(entityManager);
		AbstractEntity.setRepository(repository);
	}

	@After
	public void tearDown() {
		tx.rollback();
		entityManager.close();
		AbstractEntity.setRepository(null);
	}

	@Test
	public void testAddAndRemove() {
		Dictionary dictionary = new Dictionary("2001", "双硕士", DictionaryCategory.getByName(DictionaryCategory.DEGREE));
		dictionary = repository.save(dictionary);
		assertNotNull(dictionary.getId());
		repository.remove(dictionary);
		assertNull(repository.get(Dictionary.class, dictionary.getId()));
	}

	@Test
	public void testValidateFailure() {
		Dictionary dictionary = new Dictionary("", "", DictionaryCategory.getByName(DictionaryCategory.DEGREE));
		try {
			dictionary.save();
			fail("应抛出异常！");
		} catch (ValidationException e) {
			System.out.println(e.getMessage());
			assertTrue(true);
		}
	}
	
	@Test
	public void testExistsById() {
		assertTrue(repository.exists(Dictionary.class, 1L));
		assertFalse(repository.exists(Dictionary.class, 1000L));
	}

	@Test
	public void testGet() {
		assertEquals("男", repository.get(Dictionary.class, 1L).getText());
	}

	//@Test
	public void testLoad() {
		assertEquals("男", repository.load(Dictionary.class, 1L).getText());
	}

	@Test
	public void testGetUnmodified() {
		Dictionary dictionary = repository.get(Dictionary.class, 1L);
		dictionary.setText("xyz");
		Dictionary unmodified = repository.getUnmodified(Dictionary.class, dictionary);
		assertEquals("男", unmodified.getText());
		assertEquals("xyz", dictionary.getText());
	}

	@Test
	public void testFindQueryStringArrayParams() {
		DictionaryCategory category = DictionaryCategory.getByName(DictionaryCategory.GENDER);
		String queryString = "select o from  Dictionary o where o.category = ?";
		Object[] params = new Object[] { category };
		List<Dictionary> results = repository.find(queryString, params, Dictionary.class);
		for (Dictionary dictionary : results) {
			assertEquals(category, dictionary.getCategory());
		}
	}

	@Test
	public void testFindQueryStringMapParams() {
		DictionaryCategory category = DictionaryCategory.getByName(DictionaryCategory.GENDER);
		String queryString = "select o from  Dictionary o where o.category = :category";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", category);
		List<Dictionary> results = repository.find(queryString, params, Dictionary.class);
		for (Dictionary dictionary : results) {
			assertEquals(category, dictionary.getCategory());
		}
	}

	@Test
	public void testFindNamedQueryArrayParams() {
		DictionaryCategory category = DictionaryCategory.getByName(DictionaryCategory.GENDER);
		String code = "1";
		Object[] params = new Object[] { category, code };
		List<Dictionary> results = repository.findByNamedQuery("findByCategoryAndCode", params, Dictionary.class);
		for (Dictionary dictionary : results) {
			assertEquals(category, dictionary.getCategory());
			assertEquals(code, dictionary.getCode());
		}
	}

	@Test
	public void testFindNamedQueryMapParams() {
		DictionaryCategory category = DictionaryCategory.getByName(DictionaryCategory.GENDER);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", category);
		List<Dictionary> results = repository.findByNamedQuery("findByCategory", params, Dictionary.class);
		for (Dictionary dictionary : results) {
			assertEquals(category, dictionary.getCategory());
		}
	}

	//JPA暂时不支持按例查询
	//@Test
	public void testFindByExample() {
		Dictionary dictionary1 = Dictionary.get(20L);
		Dictionary dictionary2 = Dictionary.get(21L);
		Dictionary dictionary3 = Dictionary.get(22L);
		Dictionary example = new Dictionary(null, "技工学校", null);
		List<Dictionary> dictionaries = repository.findByExample(example, ExampleSettings.create(Dictionary.class).excludeZeroes());
		assertTrue(dictionaries.contains(dictionary1));
		assertFalse(dictionaries.contains(dictionary2));
		assertFalse(dictionaries.contains(dictionary3));
		dictionaries = repository.findByExample(example, ExampleSettings.create(Dictionary.class).excludeZeroes().enableLike());
		assertTrue(dictionaries.contains(dictionary1));
		assertTrue(dictionaries.contains(dictionary2));
		assertTrue(dictionaries.contains(dictionary3));
	}

	@Test
	public void testGetSingleResultSettings() {
		DictionaryCategory category = DictionaryCategory.getByName(DictionaryCategory.GENDER);
		String code = "1";
		QuerySettings<Dictionary> settings = QuerySettings.create(Dictionary.class)
			.eq("category", category).eq("code", code);
		Dictionary dictionary = repository.getSingleResult(settings);
		assertEquals(category, dictionary.getCategory());
		assertEquals(code, dictionary.getCode());
	}

	@Test
	public void testGetSingleResultArray() {
		DictionaryCategory category = DictionaryCategory.getByName(DictionaryCategory.GENDER);
		String code = "1";
		String queryString = "select o from  Dictionary o where o.category = ? and o.code = ?";
		Object[] params = new Object[] { category, code};
		Dictionary dictionary = (Dictionary) repository.getSingleResult(queryString, params);
		assertEquals(category, dictionary.getCategory());
		assertEquals(code, dictionary.getCode());
	}

	@Test
	public void testGetSingleResultMap() {
		DictionaryCategory category = DictionaryCategory.getByName(DictionaryCategory.GENDER);
		String code = "1";
		String queryString = "select o from  Dictionary o where o.category = :category and o.code = :code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", category);
		params.put("code", code);
		Dictionary dictionary = (Dictionary) repository.getSingleResult(queryString, params);
		assertEquals(category, dictionary.getCategory());
		assertEquals(code, dictionary.getCode());
	}

	@Test
	public void testExecuteUpdateArrayParams() {
		DictionaryCategory category = DictionaryCategory.getByName(DictionaryCategory.GENDER);
		String description = "abcd";
		String queryString = "update Dictionary o set o.description = ? where o.category = ?";
		repository.executeUpdate(queryString, new Object[] { description, category });
		QuerySettings<Dictionary> settings = QuerySettings.create(Dictionary.class).eq("category", category);
		List<Dictionary> results = repository.find(settings);
		for (Dictionary dictionary : results) {
			assertEquals(description, dictionary.getDescription());
		}
	}

	@Test
	public void testExecuteUpdateMapParams() {
		DictionaryCategory category = DictionaryCategory.getByName(DictionaryCategory.GENDER);
		String description = "abcd";
		String queryString = "update Dictionary set description = :description where category = :category";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category", category);
		params.put("description", description);
		repository.executeUpdate(queryString, params);
		QuerySettings<Dictionary> settings = QuerySettings.create(Dictionary.class).eq("category", category);
		List<Dictionary> results = repository.find(settings);
		for (Dictionary dictionary : results) {
			assertEquals(description, dictionary.getDescription());
		}
	}
}
