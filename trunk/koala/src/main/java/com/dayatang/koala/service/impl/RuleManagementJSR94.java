package com.dayatang.koala.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.koala.service.RuleManagement;
import com.dayatang.koala.service.RuleRuntimeException;

@SuppressWarnings("unchecked")
public class RuleManagementJSR94 implements RuleManagement {

	private static final long serialVersionUID = -6550908446842944288L;
	
	private RuleAdministrator ruleAdministrator;
	private LocalRuleExecutionSetProvider ruleExecutionSetProvider;
	private RuleRuntime ruleRuntime;
	

	protected static Logger log = LoggerFactory.getLogger(RuleManagementJSR94.class);

	public RuleManagementJSR94() {
	}

	public RuleManagementJSR94(RuleServiceProvider ruleServiceProvider) {
		setRuleServiceProvider(ruleServiceProvider);
	}

	public void setRuleServiceProvider(RuleServiceProvider ruleServiceProvider) {
		try {
			ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
			ruleExecutionSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(null);
			ruleRuntime = ruleServiceProvider.getRuleRuntime();
			log.info("The rule service provider of JSR94 is " + ruleServiceProvider.getClass());
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public List executeStatelessRules(String ruleSource, Map ruleProperty, Map map, List params) {
		StatelessRuleSession session = assembleStatelessRuleSession(ruleSource, ruleProperty, map);
		return executeStatelessRules(session, params);
	}

	public List executeStatelessRules(Reader ruleSource, Map ruleProperty, Map map, List params) {
		StatelessRuleSession session = assembleStatelessRuleSession(ruleSource, ruleProperty, map);
		return executeStatelessRules(session, params);
	}

	public List executeStatelessRules(InputStream ruleSource, Map ruleProperty, Map map, List params) {
		StatelessRuleSession session = assembleStatelessRuleSession(ruleSource, ruleProperty, map);
		return executeStatelessRules(session, params);
	}

	public List executeStatelessRules(Object ruleSource, Map ruleProperty, Map map, List params) {
		StatelessRuleSession session = assembleStatelessRuleSession(ruleSource, ruleProperty, map);
		return executeStatelessRules(session, params);
	}

	public StatelessRuleSession assembleStatelessRuleSession(InputStream ruleSource, Map ruleProperty, Map map) {
		try {
			return assembleStatelessRuleSession(createRuleExecutionSet(ruleSource, ruleProperty), map);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public StatelessRuleSession assembleStatelessRuleSession(Reader ruleSource, Map ruleProperty, Map map) {
		try {
			return assembleStatelessRuleSession(createRuleExecutionSet(ruleSource, ruleProperty), map);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public StatelessRuleSession assembleStatelessRuleSession(Object ruleSource, Map ruleProperty, Map map) {
		try {
			return assembleStatelessRuleSession(createRuleExecutionSet(ruleSource, ruleProperty), map);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public StatelessRuleSession assembleStatelessRuleSession(String ruleSource, Map ruleProperty, Map map) {
		try {
			return assembleStatelessRuleSession(createRuleExecutionSet(ruleSource, ruleProperty), map);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	private StatelessRuleSession assembleStatelessRuleSession(RuleExecutionSet ruleExecutionSet, Map map) {
		try{
			String packageName = ruleExecutionSet.getName();
			ruleAdministrator.registerRuleExecutionSet(packageName, ruleExecutionSet, null);
			return (StatelessRuleSession) ruleRuntime.createRuleSession(packageName, map, RuleRuntime.STATELESS_SESSION_TYPE);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	private List executeStatelessRules(StatelessRuleSession statelessSession, List params) {
		List result;
		StopWatch watch = null;
		if (log.isDebugEnabled()) {
			watch = new StopWatch();
			watch.start();
		}

		try{
			result = statelessSession.executeRules(params);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}

		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Rule session Assembled in " + watch);
			// for gc
			watch = null;
		}
		return result;
	}

	private RuleExecutionSet createRuleExecutionSet(String ruleSource, Map ruleProperty) throws RuleExecutionSetCreateException, IOException {
		return ruleExecutionSetProvider.createRuleExecutionSet(new StringReader(ruleSource), ruleProperty);
	}

	private RuleExecutionSet createRuleExecutionSet(InputStream ruleSource, Map ruleProperty) throws RuleExecutionSetCreateException, IOException {
		return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, ruleProperty);
	}

	private RuleExecutionSet createRuleExecutionSet(Reader ruleSource, Map ruleProperty) throws RuleExecutionSetCreateException, IOException {
		return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, ruleProperty);
	}

	private RuleExecutionSet createRuleExecutionSet(Object ruleSource, Map ruleProperty) throws RuleExecutionSetCreateException {
		return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, ruleProperty);
	}
}
