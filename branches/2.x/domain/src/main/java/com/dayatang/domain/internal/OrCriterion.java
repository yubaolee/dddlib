package com.dayatang.domain.internal;

import com.dayatang.domain.QueryCriterion;

public class OrCriterion implements QueryCriterion {
	private QueryCriterion[] criterons;

	public OrCriterion(QueryCriterion... criterons) {
		this.criterons = criterons;
	}

	public QueryCriterion[] getCriterons() {
		return criterons;
	}
}
