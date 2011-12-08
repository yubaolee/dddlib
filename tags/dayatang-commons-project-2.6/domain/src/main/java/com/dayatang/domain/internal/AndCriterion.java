package com.dayatang.domain.internal;

import com.dayatang.domain.QueryCriterion;

public class AndCriterion implements QueryCriterion {
	private QueryCriterion[] criterons;

	public AndCriterion(QueryCriterion... criterons) {
		this.criterons = criterons;
	}

	public QueryCriterion[] getCriterons() {
		return criterons;
	}
}
