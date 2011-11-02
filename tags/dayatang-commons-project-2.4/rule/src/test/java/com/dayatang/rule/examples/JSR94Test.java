package com.dayatang.rule.examples;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;

import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.Test;

import com.dayatang.rule.StatefulRuleService;
import com.dayatang.rule.StatelessRuleService;
import com.dayatang.rule.impl.StatefulRuleServiceJsr94;
import com.dayatang.rule.impl.StatelessRuleServiceJsr94;

public class JSR94Test {

	private String ruleDrl = "/rule/Person.drl";

	@SuppressWarnings("unchecked")
	@Test
	public void stateless() throws Exception {
		// Execute rule
		List<Person> statelessList = new ArrayList<Person>();
		statelessList.add(new Person(1L, "chencao"));
		StatelessRuleService ruleService = new StatelessRuleServiceJsr94(new RuleServiceProviderImpl());
		StatelessRuleSession statelessSession = ruleService.assembleRuleSession(getClass().getResourceAsStream(ruleDrl), null, null);
		List statelessResults = statelessSession.executeRules(statelessList);

		// Validate
		assertEquals(1, statelessResults.size());

		Person p = (Person) statelessResults.get(0);
		assertEquals(100, p.getId().longValue());
	}

	@Test
	public void stateful() throws Exception {
		// Execute rule
		StatefulRuleService ruleService = new StatefulRuleServiceJsr94(new RuleServiceProviderImpl());
		StatefulRuleSession statefulSession = ruleService.assembleRuleSession(getClass().getResourceAsStream(ruleDrl), null, null);
		Person firstPerson = new Person(2L, "chencao");
		statefulSession.addObject(firstPerson);
		statefulSession.executeRules();

		// Validate
		assertEquals(200, firstPerson.getId().longValue());
		assertEquals("chencao changed", firstPerson.getName());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void globalAndFunction() throws Exception {

		// Prepare global parameter
		List globalList = new ArrayList();
		Map map = new HashMap();
		map.put("list", globalList);
		StatelessRuleService ruleService = new StatelessRuleServiceJsr94(new RuleServiceProviderImpl());
		StatelessRuleSession statelessSession = ruleService.assembleRuleSession(getClass().getResourceAsStream(ruleDrl), null, map);


		// Execute rule
		List globalParams = new ArrayList();
		Person firstPerson = new Person(3L, "chencao");
		globalParams.add(firstPerson);
		statelessSession.executeRules(globalParams);

		// FirstPerson hasn't been changed
		assertEquals(300, firstPerson.getId().longValue());

		// Validate global
		List global = (List) map.get("list");
		assertEquals(2, global.size());
		Person p1 = (Person) global.get(0);
		assertEquals(300, p1.getId().longValue());
		Person p2 = (Person) global.get(1);
		assertEquals(400, p2.getId().longValue());
		assertEquals("pengmei", p2.getName());

	}

}
