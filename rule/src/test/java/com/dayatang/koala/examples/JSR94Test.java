package com.dayatang.koala.examples;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.rules.admin.RuleExecutionSetRegisterException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JSR94Test {

	String ruleDrl = "/rule/Person.drl";

	@Test
	public void stateless() throws Exception {

		// Prepare ruleRuntime
		StatelessRuleSession statelessSession = (StatelessRuleSession) RuleAssembler
				.assembleRuleSession(ruleDrl,
						RuleRuntime.STATELESS_SESSION_TYPE, null);

		// Execute rule
		List statelessList = new ArrayList();
		Person firstPerson = new Person(1L, "chencao");
		statelessList.add(firstPerson);
		List statelessResults = new ArrayList();
		statelessResults = statelessSession.executeRules(statelessList);

		// Validate
		assertEquals(1, statelessResults.size());

		Person p = (Person) statelessResults.get(0);
		assertEquals(100, p.getId().longValue());

		assertEquals(100, firstPerson.getId().longValue());
	}

	@Test
	public void stateful() throws Exception {

		// Prepare ruleRuntime
		StatefulRuleSession statefulSession = (StatefulRuleSession) RuleAssembler
				.assembleRuleSession(ruleDrl,
						RuleRuntime.STATEFUL_SESSION_TYPE, null);

		// Execute rule
		Person firstPerson = new Person(2L, "chencao");
		statefulSession.addObject(firstPerson);
		statefulSession.executeRules();

		// Validate
		assertEquals(200, firstPerson.getId().longValue());
		assertEquals("chencao changed", firstPerson.getName());
	}

	@Test
	public void globalAndFunction() throws Exception {

		// Prepare global parameter
		java.util.List globalList = new java.util.ArrayList();
		java.util.Map map = new java.util.HashMap();
		map.put("list", globalList);

		// Prepare ruleRuntime
		StatelessRuleSession globalStatelessSession = (StatelessRuleSession) RuleAssembler
				.assembleRuleSession(ruleDrl,
						RuleRuntime.STATELESS_SESSION_TYPE, map);

		// Execute rule
		List globalParams = new ArrayList();
		Person firstPerson = new Person(3L, "chencao");
		globalParams.add(firstPerson);
		List globalStatelessResults = new ArrayList();
		globalStatelessResults = globalStatelessSession
				.executeRules(globalParams);

		// FirstPerson hasn't been changed
		assertEquals(300, firstPerson.getId().longValue());

		// Validate global
		List global = (java.util.List) map.get("list");
		assertEquals(2, global.size());
		Person p1 = (Person) global.get(0);
		assertEquals(300, p1.getId().longValue());
		Person p2 = (Person) global.get(1);
		assertEquals(400, p2.getId().longValue());
		assertEquals("pengmei", p2.getName());

	}

	private String registerExecutionSet(RuleAdministrator ruleAdministrator,
			LocalRuleExecutionSetProvider ruleExecutionSetProvider,
			String drlFile) throws RuleExecutionSetCreateException,
			IOException, RuleExecutionSetRegisterException, RemoteException {

		RuleExecutionSet ruleExecutionSet = getLocalExecutionSet(
				ruleExecutionSetProvider, drlFile);

		// Register the RuleExecutionSet with the RuleAdministrator
		String uri = ruleExecutionSet.getName();

		ruleAdministrator.registerRuleExecutionSet(uri, ruleExecutionSet, null);
		return uri;
	}

	private RuleExecutionSet getLocalExecutionSet(
			LocalRuleExecutionSetProvider ruleExecutionSetProvider,
			String drlFile) throws RuleExecutionSetCreateException, IOException {
		// Create a Reader for the drl
		// URL drlUrl = new URL("http://mydomain.org/sources/myrules.drl");
		// Reader drlReader = new InputStreamReader(drlUrl.openStream());

		Reader drlReader = new InputStreamReader(JSR94Test.class
				.getResourceAsStream(drlFile));

		// Create the RuleExecutionSet for the drl
		RuleExecutionSet ruleExecutionSet = ruleExecutionSetProvider
				.createRuleExecutionSet(drlReader, null);
		return ruleExecutionSet;
	}
}
