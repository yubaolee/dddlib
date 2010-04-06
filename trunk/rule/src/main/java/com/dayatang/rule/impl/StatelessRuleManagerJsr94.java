package com.dayatang.rule.impl;

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

import com.dayatang.rule.RuleRuntimeException;
import com.dayatang.rule.StatelessRuleManager;
import com.dayatang.rule.UnSupportedRuleFormatException;

@SuppressWarnings("unchecked")
public class StatelessRuleManagerJsr94 implements StatelessRuleManager {

	private static final long serialVersionUID = -6550908446842944288L;
	
	private RuleAdministrator ruleAdministrator;
	private LocalRuleExecutionSetProvider ruleExecutionSetProvider;
	private RuleRuntime ruleRuntime;
	

	protected static Logger log = LoggerFactory.getLogger(StatelessRuleManagerJsr94.class);

	public StatelessRuleManagerJsr94() {
	}

	public StatelessRuleManagerJsr94(RuleServiceProvider ruleServiceProvider) {
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

	public List executeRules(String ruleSource, Map ruleProperty, Map map, List params) {
		StatelessRuleSession session = assembleRuleSession(ruleSource, ruleProperty, map);
		return executeRules(session, params);
	}

	public List executeRules(Reader ruleSource, Map ruleProperty, Map map, List params) {
		StatelessRuleSession session = assembleRuleSession(ruleSource, ruleProperty, map);
		return executeRules(session, params);
	}

	public List executeRules(InputStream ruleSource, Map ruleProperty, Map map, List params) {
		StatelessRuleSession session = assembleRuleSession(ruleSource, ruleProperty, map);
		return executeRules(session, params);
	}

	public List executeRules(Object ruleSource, Map ruleProperty, Map map, List params) {
		StatelessRuleSession session = assembleRuleSession(ruleSource, ruleProperty, map);
		return executeRules(session, params);
	}

	public StatelessRuleSession assembleRuleSession(InputStream ruleSource, Map ruleProperty, Map map) {
		try {
			return assembleRuleSession(createRuleExecutionSet(ruleSource, ruleProperty), map);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public StatelessRuleSession assembleRuleSession(Reader ruleSource, Map ruleProperty, Map map) {
		try {
			return assembleRuleSession(createRuleExecutionSet(ruleSource, ruleProperty), map);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public StatelessRuleSession assembleRuleSession(Object ruleSource, Map ruleProperty, Map map) {
		try {
			return assembleRuleSession(createRuleExecutionSet(ruleSource, ruleProperty), map);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public StatelessRuleSession assembleRuleSession(String ruleSource, Map ruleProperty, Map map) {
		try {
			return assembleRuleSession(createRuleExecutionSet(ruleSource, ruleProperty), map);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	private StatelessRuleSession assembleRuleSession(RuleExecutionSet ruleExecutionSet, Map map) {
		try{
			String packageName = ruleExecutionSet.getName();
			ruleAdministrator.registerRuleExecutionSet(packageName, ruleExecutionSet, null);
			return (StatelessRuleSession) ruleRuntime.createRuleSession(packageName, map, RuleRuntime.STATELESS_SESSION_TYPE);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	private List executeRules(StatelessRuleSession statelessSession, List params) {
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
