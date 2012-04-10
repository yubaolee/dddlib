package com.dayatang.rule;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.StatefulRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class StatefulRuleTemplate {

	private RuleAdministrator ruleAdministrator;
	private LocalRuleExecutionSetProvider ruleExecutionSetProvider;
	private RuleRuntime ruleRuntime;

	private RuleExecutionSet ruleExecutionSet;
	private Map sessionProperties;
	
	protected static Logger LOGGER = LoggerFactory.getLogger(StatefulRuleTemplate.class);

	/**
	 * 构造函数
	 * @param ruleServiceProvider 规则服务提供者实现类，如Drools等。
	 * @param serviceProviderProperties 具体规则服务提供者所需要的额外属性
	 * @param ruleSource 规则源，包含规则定义的内容。可能是字符串，Reader, InputStream或其他服务提供者支持的类型。
	 * @param executionSetProperties 规则的属性Map(如：source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 */
	public StatefulRuleTemplate(RuleServiceProvider ruleServiceProvider, Map serviceProviderProperties, Object ruleSource, Map executionSetProperties, Map sessionProperties) {
		try {
			ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
			ruleExecutionSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(serviceProviderProperties);
			ruleRuntime = ruleServiceProvider.getRuleRuntime();
			this.ruleExecutionSet = createRuleExecutionSet(ruleSource, executionSetProperties);
			LOGGER.info("The rule service provider of JSR94 is " + ruleServiceProvider.getClass());
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
		this.sessionProperties = sessionProperties;
	}

	private RuleExecutionSet createRuleExecutionSet(Object ruleSource, Map executionSetProperties) {
		try {
			if (ruleSource instanceof String) {
				Reader reader = new StringReader((String) ruleSource);
				return ruleExecutionSetProvider.createRuleExecutionSet(reader, executionSetProperties);
			}
			if (ruleSource instanceof Reader) {
				Reader reader = (Reader) ruleSource;
				return ruleExecutionSetProvider.createRuleExecutionSet(reader, executionSetProperties);
			}
			if (ruleSource instanceof InputStream) {
				InputStream in = (InputStream) ruleSource;
				return ruleExecutionSetProvider.createRuleExecutionSet(in, executionSetProperties);
			}
			return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, executionSetProperties);

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
			ruleAdministrator.registerRuleExecutionSet(packageName, ruleExecutionSet, null);
			return (StatefulRuleSession) ruleRuntime.createRuleSession(packageName, sessionProperties, RuleRuntime.STATEFUL_SESSION_TYPE);
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
