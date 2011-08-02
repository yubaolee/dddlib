package com.dayatang.spring.repository.internal;

import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import com.dayatang.domain.Entity;
import com.dayatang.domain.OrderSettings;
import com.dayatang.domain.QueryCriterion;
import com.dayatang.domain.QuerySettings;

public class HibernateCriteriaBuilder {

	public static final DetachedCriteria createCriteria(QuerySettings<? extends Entity> settings) {
		DetachedCriteria criteria = DetachedCriteria.forClass(settings.getEntityClass());
		for (Map.Entry<String, String> aliasEntry : settings.getAliases().entrySet()) {
			criteria.createAlias(aliasEntry.getKey(), aliasEntry.getValue());
		}
		for (QueryCriterion criterion : settings.getCriterions()) {
			Criterion hibernateCriterion = HibernateCriterionConverter.convert(criterion, criteria);
			if (hibernateCriterion != null) {
				criteria.add(hibernateCriterion);
			}
		}
		for (OrderSettings orderSettings : settings.getOrderSettings()) {
			criteria.addOrder(toOrder(orderSettings));
		}
		//System.out.println(criteria.);
		return criteria;
	}

	private static Order toOrder(OrderSettings orderSettings) {
		if (orderSettings.isAscending()) {
			return Order.asc(orderSettings.getPropName());
		}
		return Order.desc(orderSettings.getPropName());
	}
	
}
