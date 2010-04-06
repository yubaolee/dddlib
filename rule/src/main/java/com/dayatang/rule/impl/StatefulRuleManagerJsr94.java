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

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.rule.RuleRuntimeException;
import com.dayatang.rule.StatefulRuleManager;
import com.dayatang.rule.UnSupportedRuleFormatException;

@SuppressWarnings("unchecked")
public class StatefulRuleManagerJsr94 implements StatefulRuleManager {

	private static final long serialVersionUID = -6550908446842944288L;
	
	private RuleAdministrator ruleAdministrator;
	private LocalRuleExecutionSetProvider ruleExecutionSetProvider;
	private RuleRuntime ruleRuntime;
	

	protected static Logger log = LoggerFactory.getLogger(StatefulRuleManagerJsr94.class);

	public StatefulRuleManagerJsr94() {
	}

	public StatefulRuleManagerJsr94(RuleServiceProvider ruleServiceProvider) {
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

	public void executeRules(String ruleSource, Map ruleProperty, Map map, List params) {
		StatefulRuleSession session = assembleRuleSession(ruleSource, ruleProperty, map);
		executeRules(session, params);
	}

	public void executeRules(Reader ruleSource, Map ruleProperty, Map map, List params) {
		StatefulRuleSession session = assembleRuleSession(ruleSource, ruleProperty, map);
		executeRules(session, params);
	}

	public void executeRules(InputStream ruleSource, Map ruleProperty, Map map, List params) {
		StatefulRuleSession session = assembleRuleSession(ruleSource, ruleProperty, map);
		executeRules(session, params);
	}

	public void executeRules(Object ruleSource, Map ruleProperty, Map map, List params) {
		StatefulRuleSession session = assembleRuleSession(ruleSource, ruleProperty, map);
		executeRules(session, params);
	}

	public StatefulRuleSession assembleRuleSession(InputStream ruleSource, Map ruleProperty, Map map) {
		try {
			return assembleRuleSession(createRuleExecutionSet(ruleSource, ruleProperty), map);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public StatefulRuleSession assembleRuleSession(Reader ruleSource, Map ruleProperty, Map map) {
		try {
			return assembleRuleSession(createRuleExecutionSet(ruleSource, ruleProperty), map);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public StatefulRuleSession assembleRuleSession(Object ruleSource, Map ruleProperty, Map map) {
		try {
			return assembleRuleSession(createRuleExecutionSet(ruleSource, ruleProperty), map);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public StatefulRuleSession assembleRuleSession(String ruleSource, Map ruleProperty, Map map) {
		try {
			return assembleRuleSession(createRuleExecutionSet(ruleSource, ruleProperty), map);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	private StatefulRuleSession assembleRuleSession(RuleExecutionSet ruleExecutionSet, Map map) {
		try{
			String packageName = ruleExecutionSet.getName();
			ruleAdministrator.registerRuleExecutionSet(packageName, ruleExecutionSet, null);
			return (StatefulRuleSession) ruleRuntime.createRuleSession(packageName, map, RuleRuntime.STATEFUL_SESSION_TYPE);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	private void executeRules(StatefulRuleSession statefulSession, List params) {
		List result;
		StopWatch watch = null;
		if (log.isDebugEnabled()) {
			watch = new StopWatch();
			watch.start();
		}

		try{
			statefulSession.executeRules();
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}

		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Rule session Assembled in " + watch);
			// for gc
			watch = null;
		}
	}

	private RuleExecutionSet createRuleExecutionSet(String ruleSource, Map ruleProperty) {
		try {
			return ruleExecutionSetProvider.createRuleExecutionSet(new StringReader(ruleSource), ruleProperty);
		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private RuleExecutionSet createRuleExecutionSet(InputStream ruleSource, Map ruleProperty) {
		try {
			return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, ruleProperty);
		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private RuleExecutionSet createRuleExecutionSet(Reader ruleSource, Map ruleProperty) {
		try {
			return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, ruleProperty);
		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private RuleExecutionSet createRuleExecutionSet(Object ruleSource, Map ruleProperty) {
		try {
			return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, ruleProperty);
		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		}
	}
}
