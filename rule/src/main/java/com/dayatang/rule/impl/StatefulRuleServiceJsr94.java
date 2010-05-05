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
import com.dayatang.rule.StatefulRuleService;
import com.dayatang.rule.UnSupportedRuleFormatException;

@SuppressWarnings("unchecked")
public class StatefulRuleServiceJsr94 implements StatefulRuleService {

	private static final long serialVersionUID = -6550908446842944288L;
	
	private RuleAdministrator ruleAdministrator;
	private LocalRuleExecutionSetProvider ruleExecutionSetProvider;
	private RuleRuntime ruleRuntime;
	

	protected static Logger log = LoggerFactory.getLogger(StatefulRuleServiceJsr94.class);

	public StatefulRuleServiceJsr94() {
	}

	public StatefulRuleServiceJsr94(RuleServiceProvider ruleServiceProvider) {
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

	public void executeRules(String ruleSource, Map executionSetProperties, Map sessionProperties, List params) {
		StatefulRuleSession session = assembleRuleSession(ruleSource, executionSetProperties, sessionProperties);
		executeRules(session, params);
	}

	public void executeRules(Reader ruleSource, Map executionSetProperties, Map sessionProperties, List params) {
		StatefulRuleSession session = assembleRuleSession(ruleSource, executionSetProperties, sessionProperties);
		executeRules(session, params);
	}

	public void executeRules(InputStream ruleSource, Map executionSetProperties, Map sessionProperties, List params) {
		StatefulRuleSession session = assembleRuleSession(ruleSource, executionSetProperties, sessionProperties);
		executeRules(session, params);
	}

	public void executeRules(Object ruleSource, Map executionSetProperties, Map sessionProperties, List params) {
		StatefulRuleSession session = assembleRuleSession(ruleSource, executionSetProperties, sessionProperties);
		executeRules(session, params);
	}

	public StatefulRuleSession assembleRuleSession(InputStream ruleSource, Map executionSetProperties, Map sessionProperties) {
		StatefulRuleSession result = null;
		try {
			result = assembleRuleSession(createRuleExecutionSet(ruleSource, executionSetProperties), sessionProperties);
			ruleSource.close();
			return result;
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public StatefulRuleSession assembleRuleSession(Reader ruleSource, Map executionSetProperties, Map sessionProperties) {
		StatefulRuleSession result = null;
		try {
			result = assembleRuleSession(createRuleExecutionSet(ruleSource, executionSetProperties), sessionProperties);
			ruleSource.close();
			return result;
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public StatefulRuleSession assembleRuleSession(Object ruleSource, Map executionSetProperties, Map sessionProperties) {
		try {
			return assembleRuleSession(createRuleExecutionSet(ruleSource, executionSetProperties), sessionProperties);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	public StatefulRuleSession assembleRuleSession(String ruleSource, Map executionSetProperties, Map sessionProperties) {
		try {
			return assembleRuleSession(createRuleExecutionSet(ruleSource, executionSetProperties), sessionProperties);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	private StatefulRuleSession assembleRuleSession(RuleExecutionSet ruleExecutionSet, Map sessionProperties) {
		try{
			String packageName = ruleExecutionSet.getName();
			ruleAdministrator.registerRuleExecutionSet(packageName, ruleExecutionSet, null);
			return (StatefulRuleSession) ruleRuntime.createRuleSession(packageName, sessionProperties, RuleRuntime.STATEFUL_SESSION_TYPE);
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	private void executeRules(StatefulRuleSession statefulSession, List params) {
		StopWatch watch = null;
		if (log.isDebugEnabled()) {
			watch = new StopWatch();
			watch.start();
		}

		try{
			statefulSession.executeRules();
			statefulSession.release();
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

	private RuleExecutionSet createRuleExecutionSet(String ruleSource, Map executionSetProperties) {
		try {
			return ruleExecutionSetProvider.createRuleExecutionSet(new StringReader(ruleSource), executionSetProperties);
		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
}
