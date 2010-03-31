package com.dayatang.koala.time;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.rules.RuleRuntime;
import javax.rules.StatelessRuleSession;

import org.junit.Test;

import com.dayatang.koala.examples.RuleStringAssembler;

public class FooTest {

	String ruleDrl = "/rule/Foo.drl";

	@Test
	public void item1() throws Exception {

		StatelessRuleSession statelessSession = (StatelessRuleSession) RuleStringAssembler
				.assembleRuleSession(ruleDrl,
						RuleRuntime.STATELESS_SESSION_TYPE, null);

		// System.setProperty("drools.dateformat", "yyyy-mm-dd");
		// System.out.println(System.getProperty("drools.dateformat"));

		String format = "dd-MMM-yyyy";
		DateFormatSymbols dateSymbol = new DateFormatSymbols(new Locale(Locale
				.getDefault().getLanguage(), Locale.getDefault().getCountry()));
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, dateSymbol);
		Date date = dateFormat.parse("20-六月-2007");
		System.out.println(date);

		Foo foo1 = new Foo();
		foo1.setId(1L);
		foo1.setName("foo1");
		foo1.setResult("foo1");
		foo1.setStartDate(new Date());

		Foo foo2 = new Foo();
		foo2.setId(1L);
		foo2.setName("foo2");
		foo2.setResult("foo2");
		foo2.setStartDate(new Date());

		List params = new ArrayList();
		params.add(foo1);
		params.add(foo2);
		List globalStatelessResults = new ArrayList();
		globalStatelessResults = statelessSession.executeRules(params);

	}
}
