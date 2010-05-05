package com.dayatang.rule.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.rules.StatelessRuleSession;

import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.rule.StatelessRuleService;
import com.dayatang.rule.impl.StatelessRuleServiceJsr94;

public class RuleTest {

	protected StatelessRuleService ruleService;

	@Before
	public void setup() throws Exception {
		ruleService = new StatelessRuleServiceJsr94(new RuleServiceProviderImpl());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void cc() throws Exception {
		Map map = new HashMap();
		Map globalMap = new HashMap();
		map.put("map", globalMap);
		StatelessRuleSession ruleSession = ruleService.assembleRuleSession(
				RuleTest.class.getResourceAsStream("/rule/example.drl"), 
				null, 
				null);

		ruleSession.executeRules(new ArrayList());
		System.out.println(globalMap.get("cc"));
	}
}
