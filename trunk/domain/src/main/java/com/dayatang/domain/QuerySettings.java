package com.dayatang.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
import com.dayatang.domain.internal.NotEmptyCriteron;
import com.dayatang.domain.internal.NotEqCriteron;
import com.dayatang.domain.internal.NotEqPropCriteron;
import com.dayatang.domain.internal.NotInCriteron;
import com.dayatang.domain.internal.NotNullCriteron;
import com.dayatang.domain.internal.SizeEqCriteron;
import com.dayatang.domain.internal.SizeGeCriteron;
import com.dayatang.domain.internal.SizeGtCriteron;
import com.dayatang.domain.internal.SizeLeCriteron;
import com.dayatang.domain.internal.SizeLtCriteron;
import com.dayatang.domain.internal.SizeNotEqCriteron;
import com.dayatang.domain.internal.StartsWithTextCriteron;

/**
 * 数据仓储查询设定对象,用来收集各种查询设定,如过滤条件,分页信息,排序信息等等.
 * @author cnyangyu
 *
 */
public class QuerySettings<T> {
	
	private Class<T> entityClass;
	private Set<QueryCriteron> criterons = new HashSet<QueryCriteron>();
	private int firstResult;
	private int maxResults;
	private List<OrderSettings> orderSettings = new ArrayList<OrderSettings>();

	public static <T extends Entity> QuerySettings<T> create(Class<T> entityClass) {
		return new QuerySettings<T>(entityClass);
	}
	
	private QuerySettings(Class<T> entityClass) {
		super();
		this.entityClass = entityClass;
	}

	/**
	 * @return the entityClass
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	public Set<QueryCriteron> getCriterons() {
		return criterons;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public List<OrderSettings> getOrderSettings() {
		return orderSettings;
	}

	public QuerySettings<T> eq(String propName, Object value) {
		criterons.add(new EqCriteron(propName, value));
		return this;
	}
	
	public QuerySettings<T> notEq(String propName, Object value) {
		criterons.add(new NotEqCriteron(propName, value));
		return this;
	}
	
	public QuerySettings<T> ge(String propName, Object value) {
		criterons.add(new GeCriteron(propName, value));
		return this;
	}
	
	public QuerySettings<T> gt(String propName, Object value) {
		criterons.add(new GtCriteron(propName, value));
		return this;
	}
	
	public QuerySettings<T> le(String propName, Object value) {
		criterons.add(new LeCriteron(propName, value));
		return this;
	}
	
	public QuerySettings<T> lt(String propName, Object value) {
		criterons.add(new LtCriteron(propName, value));
		return this;
	}
	
	public QuerySettings<T> eqProp(String propName, String otherProp) {
		criterons.add(new EqPropCriteron(propName, otherProp));
		return this;
	}
	
	public QuerySettings<T> notEqProp(String propName, String otherProp) {
		criterons.add(new NotEqPropCriteron(propName, otherProp));
		return this;
	}
	
	public QuerySettings<T> gtProp(String propName, String otherProp) {
		criterons.add(new GtPropCriteron(propName, otherProp));
		return this;
	}
	
	public QuerySettings<T> geProp(String propName, String otherProp) {
		criterons.add(new GePropCriteron(propName, otherProp));
		return this;
	}
	
	public QuerySettings<T> ltProp(String propName, String otherProp) {
		criterons.add(new LtPropCriteron(propName, otherProp));
		return this;
	}
	
	public QuerySettings<T> leProp(String propName, String otherProp) {
		criterons.add(new LePropCriteron(propName, otherProp));
		return this;
	}
	
	public QuerySettings<T> sizeEq(String propName, int size) {
		criterons.add(new SizeEqCriteron(propName, size));
		return this;
	}
	
	public QuerySettings<T> sizeNotEq(String propName, int size) {
		criterons.add(new SizeNotEqCriteron(propName, size));
		return this;
	}
	
	public QuerySettings<T> sizeGt(String propName, int size) {
		criterons.add(new SizeGtCriteron(propName, size));
		return this;
	}
	
	public QuerySettings<T> sizeGe(String propName, int size) {
		criterons.add(new SizeGeCriteron(propName, size));
		return this;
	}
	
	public QuerySettings<T> sizeLt(String propName, int size) {
		criterons.add(new SizeLtCriteron(propName, size));
		return this;
	}
	
	public QuerySettings<T> sizeLe(String propName, int size) {
		criterons.add(new SizeLeCriteron(propName, size));
		return this;
	}

	public QuerySettings<T> containsText(String propName, String value) {
		criterons.add(new ContainsTextCriteron(propName, value));
		return this;
	}

	public QuerySettings<T> startsWithText(String propName, String value) {
		criterons.add(new StartsWithTextCriteron(propName, value));
		return this;
	}

	public QuerySettings<T> in(String propName, Collection<? extends Object> value) {
		criterons.add(new InCriteron(propName, value));
		return this;
	}

	public QuerySettings<T> in(String propName, Object[] value) {
		criterons.add(new InCriteron(propName, value));
		return this;
	}

	public QuerySettings<T> notIn(String propName, Collection<? extends Object> value) {
		criterons.add(new NotInCriteron(propName, value));
		return this;
	}

	public QuerySettings<T> notIn(String propName, Object[] value) {
		criterons.add(new NotInCriteron(propName, value));
		return this;
	}

	public QuerySettings<T> between(String propName, Object from, Object to) {
		criterons.add(new BetweenCriteron(propName, from, to));
		return this;
	}
	
	public QuerySettings<T> isNull(String propName) {
		criterons.add(new IsNullCriteron(propName));
		return this;
	}
	
	public QuerySettings<T> notNull(String propName) {
		criterons.add(new NotNullCriteron(propName));
		return this;
	}
	
	public QuerySettings<T> isEmpty(String propName) {
		criterons.add(new IsEmptyCriteron(propName));
		return this;
	}
	
	public QuerySettings<T> notEmpty(String propName) {
		criterons.add(new NotEmptyCriteron(propName));
		return this;
	}

	/**
	 * 判断criterons中是否包含至少一个InCriteron，其value是个空集合
	 * @return
	 */
	public boolean containsInCriteronWithEmptyValue() {
		for (QueryCriteron criteron : getCriterons()) {
			if (criteron instanceof InCriteron && ((InCriteron) criteron).getValue().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public QuerySettings<T> setFirstResult(int firstResult) {
		this.firstResult = firstResult;
		return this;
	}

	public QuerySettings<T> setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public QuerySettings<T> asc(String propName) {
		orderSettings.add(OrderSettings.asc(propName));
		return this;
	}

	public QuerySettings<T> desc(String propName) {
		orderSettings.add(OrderSettings.desc(propName));
		return this;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof QuerySettings))
			return false;
		QuerySettings castOther = (QuerySettings) other;
		return new EqualsBuilder()
				.append(entityClass, castOther.entityClass)
				.append(criterons, castOther.criterons)
				.append(firstResult, castOther.firstResult)
				.append(maxResults, castOther.maxResults)
				.append(orderSettings, castOther.orderSettings)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(entityClass)
				.append(criterons)
				.append(firstResult)
				.append(maxResults)
				.append(orderSettings)
				.toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Class:").append(entityClass.getSimpleName()).append(SystemUtils.LINE_SEPARATOR);
		result.append("criterons: [");
		for (QueryCriteron criteron : criterons) {
			result.append(criteron);
		}
		result.append("]").append(SystemUtils.LINE_SEPARATOR);
		result.append("firstResult:" + firstResult).append(SystemUtils.LINE_SEPARATOR);
		result.append("maxResults" + maxResults).append(SystemUtils.LINE_SEPARATOR);
		result.append("orderSettings: [");
		for (OrderSettings orderSetting : orderSettings) {
			result.append(orderSetting);
		}
		result.append("]");
		return result.toString();
	}
	
}
