package com.dayatang.rule.impl;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import javax.rules.RuleServiceProvider;

import com.dayatang.rule.StatelessRuleService;
import com.dayatang.rule.StatelessRuleTemplate;

/**
 * 无状态规则服务的实现类，用JSR94实现。
 * @author yyang <a href="mailto:gdyangyu@gmail.com">杨宇</a>
 *
 */
@SuppressWarnings("rawtypes")
public class StatelessRuleServiceJsr94 implements StatelessRuleService {

	private static final long serialVersionUID = -6550908446842944288L;
	private StatelessRuleTemplate template;
	


	/**
	 * 构造函数
	 * @param ruleServiceProvider 规则服务提供者实现类，如Drools等
	 */
	public StatelessRuleServiceJsr94(RuleServiceProvider ruleServiceProvider) {
		this(ruleServiceProvider, null);
	}
	
	/**
	 * 构造函数。
	 * @param ruleServiceProvider 规则服务提供者实现类，如Drools等
	 * @param serviceProviderProperties 具体规则服务提供者所需要的额外属性
	 */
	public StatelessRuleServiceJsr94(RuleServiceProvider ruleServiceProvider, Map serviceProviderProperties) {
		template = new StatelessRuleTemplate(ruleServiceProvider, serviceProviderProperties);
	}

	@Override
	public List executeRules(String ruleSource, List facts) {
		return template.ruleSource(ruleSource).execute(facts);
	}

	@Override
	public List executeRules(Reader ruleSource, List facts) {
		return template.ruleSource(ruleSource).execute(facts);
	}

	@Override
	public List executeRules(InputStream ruleSource, List facts) {
		return template.ruleSource(ruleSource).execute(facts);
	}

	@Override
	public List executeRules(Object ruleSource, List facts) {
		return template.ruleSource(ruleSource).execute(facts);
	}

	@Override
	public List executeRules(String ruleSource, Map executionSetProperties, Map sessionProperties, List facts) {
		return template.sessionProperties(sessionProperties).ruleSource(ruleSource, executionSetProperties).execute(facts);
	}

	@Override
	public List executeRules(Reader ruleSource, Map executionSetProperties, Map sessionProperties, List facts) {
		return template.sessionProperties(sessionProperties).ruleSource(ruleSource, executionSetProperties).execute(facts);
	}

	@Override
	public List executeRules(InputStream ruleSource, Map executionSetProperties, Map sessionProperties, List facts) {
		return template.sessionProperties(sessionProperties).ruleSource(ruleSource, executionSetProperties).execute(facts);
	}

	@Override
	public List executeRules(Object ruleSource, Map executionSetProperties, Map sessionProperties, List facts) {
		return template.sessionProperties(sessionProperties).ruleSource(ruleSource, executionSetProperties).execute(facts);
	}
}
