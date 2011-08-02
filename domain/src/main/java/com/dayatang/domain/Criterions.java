package com.dayatang.domain;

import java.util.Collection;

import com.dayatang.domain.internal.AndCriterion;
import com.dayatang.domain.internal.BetweenCriteron;
import com.dayatang.domain.internal.ContainsTextCriteron;
import com.dayatang.domain.internal.EqCriteron;
import com.dayatang.domain.internal.EqPropCriteron;
import com.dayatang.domain.internal.GeCriteron;
import com.dayatang.domain.internal.GePropCriteron;
import com.dayatang.domain.internal.GtCriteron;
import com.dayatang.domain.internal.GtPropCriteron;
import com.dayatang.domain.internal.InCriteron;
import com.dayatang.domain.internal.IsEmptyCriteron;
import com.dayatang.domain.internal.IsNullCriteron;
import com.dayatang.domain.internal.LeCriteron;
import com.dayatang.domain.internal.LePropCriteron;
import com.dayatang.domain.internal.LtCriteron;
import com.dayatang.domain.internal.LtPropCriteron;
import com.dayatang.domain.internal.NotCriterion;
import com.dayatang.domain.internal.NotEmptyCriteron;
import com.dayatang.domain.internal.NotEqCriteron;
import com.dayatang.domain.internal.NotEqPropCriteron;
import com.dayatang.domain.internal.NotInCriteron;
import com.dayatang.domain.internal.NotNullCriteron;
import com.dayatang.domain.internal.OrCriterion;
import com.dayatang.domain.internal.SizeEqCriteron;
import com.dayatang.domain.internal.SizeGeCriteron;
import com.dayatang.domain.internal.SizeGtCriteron;
import com.dayatang.domain.internal.SizeLeCriteron;
import com.dayatang.domain.internal.SizeLtCriteron;
import com.dayatang.domain.internal.SizeNotEqCriteron;
import com.dayatang.domain.internal.StartsWithTextCriteron;

/**
 * 一个工具类，作为各种查询条件的工厂
 * @author yyang
 *
 */
public class Criterions {

	private Criterions() {
	}

	public static QueryCriterion eq(String propName, Object value) {
		return new EqCriteron(propName, value);
	}
	
	public static QueryCriterion notEq(String propName, Object value) {
		return new NotEqCriteron(propName, value);
	}
	
	public static QueryCriterion ge(String propName, Number value) {
		return new GeCriteron(propName, value);
	}
	
	public static QueryCriterion gt(String propName, Number value) {
		return new GtCriteron(propName, value);
	}
	
	public static QueryCriterion le(String propName, Number value) {
		return new LeCriteron(propName, value);
	}
	
	public static QueryCriterion lt(String propName, Number value) {
		return new LtCriteron(propName, value);
	}
	
	public static QueryCriterion eqProp(String propName, String otherProp) {
		return new EqPropCriteron(propName, otherProp);
	}
	
	public static QueryCriterion notEqProp(String propName, String otherProp) {
		return new NotEqPropCriteron(propName, otherProp);
	}
	
	public static QueryCriterion gtProp(String propName, String otherProp) {
		return new GtPropCriteron(propName, otherProp);
	}
	
	public static QueryCriterion geProp(String propName, String otherProp) {
		return new GePropCriteron(propName, otherProp);
	}
	
	public static QueryCriterion ltProp(String propName, String otherProp) {
		return new LtPropCriteron(propName, otherProp);
	}
	
	public static QueryCriterion leProp(String propName, String otherProp) {
		return new LePropCriteron(propName, otherProp);
	}
	
	public static QueryCriterion sizeEq(String propName, int size) {
		return new SizeEqCriteron(propName, size);
	}
	
	public static QueryCriterion sizeNotEq(String propName, int size) {
		return new SizeNotEqCriteron(propName, size);
	}
	
	public static QueryCriterion sizeGt(String propName, int size) {
		return new SizeGtCriteron(propName, size);
	}
	
	public static QueryCriterion sizeGe(String propName, int size) {
		return new SizeGeCriteron(propName, size);
	}
	
	public static QueryCriterion sizeLt(String propName, int size) {
		return new SizeLtCriteron(propName, size);
	}
	
	public static QueryCriterion sizeLe(String propName, int size) {
		return new SizeLeCriteron(propName, size);
	}

	public static QueryCriterion containsText(String propName, String value) {
		return new ContainsTextCriteron(propName, value);
	}

	public static QueryCriterion startsWithText(String propName, String value) {
		return new StartsWithTextCriteron(propName, value);
	}

	public static QueryCriterion in(String propName, Collection<? extends Object> value) {
		return new InCriteron(propName, value);
	}

	public static QueryCriterion in(String propName, Object[] value) {
		return new InCriteron(propName, value);
	}

	public static QueryCriterion notIn(String propName, Collection<? extends Object> value) {
		return new NotInCriteron(propName, value);
	}

	public static QueryCriterion notIn(String propName, Object[] value) {
		return new NotInCriteron(propName, value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends Object> QueryCriterion between(String propName, Comparable<T> from, Comparable<T> to) {
		return new BetweenCriteron(propName, from, to);
	}
	
	public static QueryCriterion isNull(String propName) {
		return new IsNullCriteron(propName);
	}
	
	public static QueryCriterion notNull(String propName) {
		return new NotNullCriteron(propName);
	}
	
	public static QueryCriterion isEmpty(String propName) {
		return new IsEmptyCriteron(propName);
	}
	
	public static QueryCriterion notEmpty(String propName) {
		return new NotEmptyCriteron(propName);
	}
	
	public static QueryCriterion not(QueryCriterion criterion) {
		return new NotCriterion(criterion);
	}
	
	public static QueryCriterion and(QueryCriterion... criterions) {
		return new AndCriterion(criterions);
	}
	
	public static QueryCriterion or(QueryCriterion... criterions) {
		return new OrCriterion(criterions);
	}
	
}
