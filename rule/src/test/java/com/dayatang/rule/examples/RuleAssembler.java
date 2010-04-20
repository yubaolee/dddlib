package com.dayatang.rule.examples;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.rmi.RemoteException;
import java.util.Map;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.RuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.rules.admin.RuleExecutionSetRegisterException;

public class RuleAssembler {

	public static RuleSession assembleRuleSession(String ruleDrl,
			Integer sessionType, Map params) throws Exception {
		// RuleServiceProviderImpl is registered to "http://drools.org/" via a
		// static initialization block
		Class.forName("org.drools.jsr94.rules.RuleServiceProviderImpl");

		// Get the rule service provider from the provider manager.
		RuleServiceProvider ruleServiceProvider = RuleServiceProviderManager
				.getRuleServiceProvider("http://drools.org/");

		// Get the RuleAdministration
		RuleAdministrator ruleAdministrator = ruleServiceProvider
				.getRuleAdministrator();
		LocalRuleExecutionSetProvider ruleExecutionSetProvider = ruleAdministrator
				.getLocalRuleExecutionSetProvider(null);

		String packageName = registerExecutionSet(ruleAdministrator,
				ruleExecutionSetProvider, ruleDrl);

		RuleRuntime ruleRuntime = ruleServiceProvider.getRuleRuntime();

		return ruleRuntime.createRuleSession(packageName, params, sessionType);
	}

	private static String registerExecutionSet(
			RuleAdministrator ruleAdministrator,
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

	private static RuleExecutionSet getLocalExecutionSet(
			LocalRuleExecutionSetProvider ruleExecutionSetProvider,
			String drlFile) throws RuleExecutionSetCreateException, IOException {
		// Create a Reader for the drl
		// URL drlUrl = new URL("http://mydomain.org/sources/myrules.drl");
		// Reader drlReader = new InputStreamReader(drlUrl.openStream());

		Reader drlReader = getReader(drlFile);

		// Create the RuleExecutionSet for the drl
		RuleExecutionSet ruleExecutionSet = ruleExecutionSetProvider
				.createRuleExecutionSet(drlReader, null);
		return ruleExecutionSet;
	}

	private static Reader getReader(String drlFile) {
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		Reader drlReader = new InputStreamReader(RuleAssembler.class
				.getResourceAsStream(drlFile), decoder);
		return drlReader;
	}

}
