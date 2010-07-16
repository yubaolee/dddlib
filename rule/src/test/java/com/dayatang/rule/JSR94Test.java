package com.dayatang.rule;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;

import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.rule.examples.Person;

public class JSR94Test {

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
		StatelessRuleSession statelessSession = createStatelessRuleSession();
		statelessSession.executeRules(Arrays.asList(chencao, xishi, yyang));

		// Validate
		assertEquals(60, chencao.getRetireAge());
		assertEquals(55, xishi.getRetireAge());
		assertEquals(60, yyang.getRetireAge());
		
		//Release the resources
		statelessSession.release();
	}

	private StatelessRuleSession createStatelessRuleSession() throws Exception {
		RuleServiceProvider ruleServiceProvider = new RuleServiceProviderImpl();
		RuleAdministrator ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
		LocalRuleExecutionSetProvider ruleExecutionSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(null);
		RuleExecutionSet ruleExecutionSet = ruleExecutionSetProvider.createRuleExecutionSet(getClass().getResourceAsStream(ruleDrl), null);
		String packageName = ruleExecutionSet.getName();
		ruleAdministrator.registerRuleExecutionSet(packageName, ruleExecutionSet, null);
		RuleRuntime ruleRuntime = ruleServiceProvider.getRuleRuntime();
		return (StatelessRuleSession) ruleRuntime.createRuleSession(packageName, null, RuleRuntime.STATELESS_SESSION_TYPE);
	}

	@Test
	public void stateful() throws Exception {
		// Execute rule
		StatefulRuleSession statefulSession = createStatefulRuleSession();
		statefulSession.addObject(chencao);
		statefulSession.addObject(xishi);
		statefulSession.addObject(yyang);
		statefulSession.executeRules();

		// Validate
		assertEquals(60, chencao.getRetireAge());
		assertEquals(55, xishi.getRetireAge());
		assertEquals(60, yyang.getRetireAge());
		
		//Release the resources
		statefulSession.release();
	}

	private StatefulRuleSession createStatefulRuleSession() throws Exception {
		RuleServiceProvider ruleServiceProvider = new RuleServiceProviderImpl();
		RuleAdministrator ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
		LocalRuleExecutionSetProvider ruleExecutionSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(null);
		RuleExecutionSet ruleExecutionSet = ruleExecutionSetProvider.createRuleExecutionSet(getClass().getResourceAsStream(ruleDrl), null);
		String packageName = ruleExecutionSet.getName();
		ruleAdministrator.registerRuleExecutionSet(packageName, ruleExecutionSet, null);
		RuleRuntime ruleRuntime = ruleServiceProvider.getRuleRuntime();
		return (StatefulRuleSession) ruleRuntime.createRuleSession(packageName, null, RuleRuntime.STATEFUL_SESSION_TYPE);
	}
}
