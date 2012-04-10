package com.dayatang.rule;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import javax.rules.RuleRuntime;
import javax.rules.StatefulRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;

@SuppressWarnings("rawtypes")
public class StatefulRuleTemplate {

	private StatefulRuleService ruleService;
	private RuleExecutionSet ruleExecutionSet;
	private Map sessionProperties;

	public StatefulRuleTemplate(StatefulRuleService ruleService, Object ruleSource, Map executionSetProperties, Map sessionProperties) {
		this.ruleService = ruleService;
		this.ruleExecutionSet = createRuleExecutionSet(ruleSource, executionSetProperties);
		this.sessionProperties = sessionProperties;
	}

	private RuleExecutionSet createRuleExecutionSet(Object ruleSource, Map executionSetProperties) {
		LocalRuleExecutionSetProvider executionSetProvider = ruleService.getRuleExecutionSetProvider();
		try {
			if (ruleSource instanceof String) {
				Reader reader = new StringReader((String) ruleSource);
				return executionSetProvider.createRuleExecutionSet(reader, executionSetProperties);
			}
			if (ruleSource instanceof Reader) {
				Reader reader = (Reader) ruleSource;
				return executionSetProvider.createRuleExecutionSet(reader, executionSetProperties);
			}
			if (ruleSource instanceof InputStream) {
				InputStream in = (InputStream) ruleSource;
				return executionSetProvider.createRuleExecutionSet(in, executionSetProperties);
			}
			return executionSetProvider.createRuleExecutionSet(ruleSource, executionSetProperties);

		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void execute(StatefulRuleCallback callback) throws Exception {
		StatefulRuleSession session = createStatefulRuleSession();
		try {
			callback.doInRuleSession(session);
		} finally {
			releaseStatefulRuleSession(session);
		}
	}

	private StatefulRuleSession createStatefulRuleSession() {
		try{
			String packageName = ruleExecutionSet.getName();
			ruleService.getRuleAdministrator().registerRuleExecutionSet(packageName, ruleExecutionSet, null);
			return (StatefulRuleSession) ruleService.getRuleRuntime().createRuleSession(packageName, sessionProperties, RuleRuntime.STATEFUL_SESSION_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuleRuntimeException("Cannot create Rule Session!!!", e);
		}
	}

	private void releaseStatefulRuleSession(StatefulRuleSession session) {
		try {
			session.release();
		} catch (Exception e) {
			throw new RuleRuntimeException("Cannot release rule session!!", e);
		}
	}
}
