package com.dayatang.domain.internal;

import com.dayatang.domain.QueryCriterion;

public class NotCriterion implements QueryCriterion {
	private QueryCriterion criteron;

	public NotCriterion(QueryCriterion criteron) {
		this.criteron = criteron;
	}

	public QueryCriterion getCriteron() {
		return criteron;
	}
}
