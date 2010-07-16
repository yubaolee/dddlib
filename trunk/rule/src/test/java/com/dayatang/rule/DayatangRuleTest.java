package com.dayatang.rule;

import static org.junit.Assert.*;

import java.util.Arrays;

import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;

import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.rule.examples.Person;
import com.dayatang.rule.impl.StatefulRuleServiceJsr94;
import com.dayatang.rule.impl.StatelessRuleServiceJsr94;

public class DayatangRuleTest {

	private String ruleDrl = "/Person.drl";
	private Person chencao;
	private Person xishi;
	private Person yyang;
	
	@Before
	public void setUp() {
		chencao = new Person(1L, "chencao", "male");
		xishi = new Person(2L, "xishi", "female");
		yyang = new Person(3L, "yyang", "male");
	}
	
	@Test
	public void stateless() throws Exception {
		// Execute rule
		StatelessRuleSession session = createStatelessRuleSession();
		session.executeRules(Arrays.asList(chencao, xishi, yyang));

		// Validate
		assertEquals(60, chencao.getRetireAge());
		assertEquals(55, xishi.getRetireAge());
		assertEquals(60, yyang.getRetireAge());
		
		//Release the resources
		session.release();
	}

	private StatelessRuleSession createStatelessRuleSession() {
		StatelessRuleService ruleService = new StatelessRuleServiceJsr94(new RuleServiceProviderImpl());
		return ruleService.assembleRuleSession(getClass().getResourceAsStream(ruleDrl), null, null);
	}

	@Test
	public void stateful() throws Exception {
		// Execute rule
		StatefulRuleSession session = createStatefulRuleSession();
		session.addObject(chencao);
		session.addObject(xishi);
		session.addObject(yyang);
		session.executeRules();

		// Validate
		assertEquals(60, chencao.getRetireAge());
		assertEquals(55, xishi.getRetireAge());
		assertEquals(60, yyang.getRetireAge());
		
		//Release the resources
		session.release();
	}

	private StatefulRuleSession createStatefulRuleSession() {
		StatefulRuleService ruleService = new StatefulRuleServiceJsr94(new RuleServiceProviderImpl());
		return ruleService.assembleRuleSession(getClass().getResourceAsStream(ruleDrl), null, null);
	}
}
