package com.dayatang.rule.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.StatefulRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.rule.RuleRuntimeException;
import com.dayatang.rule.StatefulRuleService;
import com.dayatang.rule.UnSupportedRuleFormatException;

@SuppressWarnings("rawtypes")
public class StatefulRuleServiceJsr94 implements StatefulRuleService {

	private static final long serialVersionUID = -6550908446842944288L;
	
	private RuleAdministrator ruleAdministrator;
	private LocalRuleExecutionSetProvider ruleExecutionSetProvider;
	private RuleRuntime ruleRuntime;
	

	protected static Logger LOGGER = LoggerFactory.getLogger(StatefulRuleServiceJsr94.class);

	public StatefulRuleServiceJsr94(RuleServiceProvider ruleServiceProvider) {
		this(ruleServiceProvider, null);
	}

	public StatefulRuleServiceJsr94(RuleServiceProvider ruleServiceProvider, Map properties) {
		try {
			ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
			ruleExecutionSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(properties);
			ruleRuntime = ruleServiceProvider.getRuleRuntime();
			LOGGER.info("The rule service provider of JSR94 is " + ruleServiceProvider.getClass());
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	@Override
	public void executeRules(String ruleSource, Map executionSetProperties, Map sessionProperties, List params) {
		RuleExecutionSet ruleExecutionSet = createRuleExecutionSet(ruleSource, executionSetProperties);
		StatefulRuleSession session = createRuleSession(ruleExecutionSet, sessionProperties);
		executeRules(session, params);
	}

	@Override
	public void executeRules(Reader ruleSource, Map executionSetProperties, Map sessionProperties, List params) {
		RuleExecutionSet ruleExecutionSet = createRuleExecutionSet(ruleSource, executionSetProperties);
		StatefulRuleSession session = createRuleSession(ruleExecutionSet, sessionProperties);
		executeRules(session, params);
	}

	@Override
	public void executeRules(InputStream ruleSource, Map executionSetProperties, Map sessionProperties, List params) {
		RuleExecutionSet ruleExecutionSet = createRuleExecutionSet(ruleSource, executionSetProperties);
		StatefulRuleSession session = createRuleSession(ruleExecutionSet, sessionProperties);
		executeRules(session, params);
	}

	@Override
	public void executeRules(Object ruleSource, Map executionSetProperties, Map sessionProperties, List params) {
		RuleExecutionSet ruleExecutionSet = createRuleExecutionSet(ruleSource, executionSetProperties);
		StatefulRuleSession session = createRuleSession(ruleExecutionSet, sessionProperties);
		executeRules(session, params);
	}

	private StatefulRuleSession createRuleSession(RuleExecutionSet ruleExecutionSet, Map sessionProperties) {
		try{
			String packageName = ruleExecutionSet.getName();
			ruleAdministrator.registerRuleExecutionSet(packageName, ruleExecutionSet, null);
			return (StatefulRuleSession) ruleRuntime.createRuleSession(packageName, sessionProperties, RuleRuntime.STATEFUL_SESSION_TYPE);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	private RuleExecutionSet createRuleExecutionSet(String ruleSource, Map executionSetProperties) {
		return createRuleExecutionSet(new StringReader(ruleSource), executionSetProperties);
	}

	private RuleExecutionSet createRuleExecutionSet(InputStream ruleSource, Map executionSetProperties) {
		try {
			return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, executionSetProperties);
		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private RuleExecutionSet createRuleExecutionSet(Reader ruleSource, Map executionSetProperties) {
		try {
			return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, executionSetProperties);
		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private RuleExecutionSet createRuleExecutionSet(Object ruleSource, Map executionSetProperties) {
		try {
			return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, executionSetProperties);
		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		}
	}

	private void executeRules(StatefulRuleSession session, List params) {
		StopWatch watch = null;
		if (LOGGER.isDebugEnabled()) {
			watch = new StopWatch();
			watch.start();
		}

		try{
			session.addObjects(params);
			session.executeRules();
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		} finally {
			try {
				session.release();
			} catch (Exception e) {
				throw new RuleRuntimeException("Cannot release rule session!!", e);
			}
		}

		if (LOGGER.isDebugEnabled()) {
			watch.stop();
			LOGGER.debug("Rule session executed in " + watch);
			// for gc
			watch = null;
		}
	}

	@Override
	public void executeRules(String ruleSource, List params) {
		executeRules(ruleSource, null, null, params);
	}

	@Override
	public void executeRules(InputStream ruleSource, List params) {
		executeRules(ruleSource, null, null, params);
	}

	@Override
	public void executeRules(Reader ruleSource, List params) {
		executeRules(ruleSource, null, null, params);
	}

	@Override
	public void executeRules(Object ruleSource, List params) {
		executeRules(ruleSource, null, null, params);
	}

	@Override
	public StatefulRuleSession getRuleSession(Object ruleSource, Map executionSetProperties, Map sessionProperties) {
		RuleExecutionSet ruleExecutionSet = createRuleExecutionSet(ruleSource, executionSetProperties);
		return createRuleSession(ruleExecutionSet, sessionProperties);
	}

	@Override
	public LocalRuleExecutionSetProvider getRuleExecutionSetProvider() {
		return ruleExecutionSetProvider;
	}

	@Override
	public RuleRuntime getRuleRuntime() {
		return ruleRuntime;
	}

	@Override
	public RuleAdministrator getRuleAdministrator() {
		return ruleAdministrator;
	}
}
