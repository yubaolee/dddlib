package com.dayatang.rule.time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.Test;

import com.dayatang.rule.StatelessRuleService;
import com.dayatang.rule.impl.StatelessRuleServiceJsr94;

public class FooTest {

	String ruleDrl = "/rule/Foo.drl";

	@Test
	public void item1() throws Exception {
		StatelessRuleService ruleService = new StatelessRuleServiceJsr94(new RuleServiceProviderImpl());
		List<?> globalStatelessResults = ruleService.executeRules(getClass().getResourceAsStream(ruleDrl), null, null, createParams());
		for (Object object : globalStatelessResults) {
			System.out.println(object);
		}
	}

	private List<Foo> createParams() {
		List<Foo> params = new ArrayList<Foo>();
		params.add(createFoo(1L, "foo1", "foo1", new Date()));
		params.add(createFoo(2L, "foo2", "foo2", new Date()));
		return params;
	}

	private Foo createFoo(long id, String name, String result, Date startDate) {
		Foo foo = new Foo();
		foo.setId(id);
		foo.setName(name);
		foo.setResult(result);
		foo.setStartDate(startDate);
		return foo;
	}
}
