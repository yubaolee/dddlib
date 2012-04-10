package com.dayatang.rule;

import javax.rules.StatefulRuleSession;

public interface StatefulRuleCallback {
	void doInRuleSession(StatefulRuleSession session) throws Exception;
}
