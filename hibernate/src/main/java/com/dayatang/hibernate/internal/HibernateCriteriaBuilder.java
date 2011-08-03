package com.dayatang.hibernate.internal;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.dayatang.domain.Entity;
import com.dayatang.domain.OrderSetting;
import com.dayatang.domain.QueryCriterion;
import com.dayatang.domain.QuerySettings;

public class HibernateCriteriaBuilder {

	public static final Criteria createCriteria(QuerySettings<? extends Entity> settings, Session session) {
		Criteria result = session.createCriteria(settings.getEntityClass());
		for (Map.Entry<String, String> aliasEntry : settings.getAliases().entrySet()) {
			result.createAlias(aliasEntry.getKey(), aliasEntry.getValue());
		}
		for (QueryCriterion criterion : settings.getCriterions()) {
			Criterion hibernateCriterion = HibernateCriterionConverter.convert(criterion, result);
			if (hibernateCriterion != null) {
				result.add(hibernateCriterion);
			}
		}
		for (OrderSetting orderSettings : settings.getOrderSettings()) {
			result.addOrder(toOrder(orderSettings));
		}
		//System.out.println(criteria.);
		return result;
	}

	private static Order toOrder(OrderSetting orderSettings) {
		if (orderSettings.isAscending()) {
			return Order.asc(orderSettings.getPropName());
		}
		return Order.desc(orderSettings.getPropName());
	}
	
}
