package com.dayatang.koala.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.rules.StatelessRuleSession;

import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.rule.StatelessRuleManager;
import com.dayatang.rule.impl.StatelessRuleManagerJsr94;

public class RuleTest {

	protected StatelessRuleManager ruleManagement;

	@Before
	public void setup() throws Exception {
		ruleManagement = new StatelessRuleManagerJsr94(new RuleServiceProviderImpl());
	}

	@Test
	public void cc() throws Exception {
		Map map = new HashMap();
		Map globalMap = new HashMap();
		map.put("map", globalMap);
		StatelessRuleSession ruleSession = ruleManagement
				.assembleRuleSession(RuleTest.class
						.getResourceAsStream("/rule/example.drl"), null, null);

		ruleSession.executeRules(new ArrayList());
		System.out.println(globalMap.get("cc"));
	}
}
