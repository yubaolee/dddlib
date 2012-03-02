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
	
	private static HibernateCriteriaBuilder instance;
	
	public static synchronized HibernateCriteriaBuilder getInstance() {
		if (instance == null) {
			instance = new HibernateCriteriaBuilder();
		}
		return instance;
	}

	public final Criteria createCriteria(QuerySettings<? extends Entity> settings, Session session) {
		Criteria result = session.createCriteria(settings.getEntityClass());
		for (Map.Entry<String, String> aliasEntry : settings.getAliases().entrySet()) {
			result.createAlias(aliasEntry.getKey(), aliasEntry.getValue());
		}
		HibernateCriterionConverter converter = HibernateCriterionConverter.getInstance();
		for (QueryCriterion criterion : settings.getCriterions()) {
			Criterion hibernateCriterion = converter.convert(criterion, result);
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

	private Order toOrder(OrderSetting orderSettings) {
		if (orderSettings.isAscending()) {
			return Order.asc(orderSettings.getPropName());
		}
		return Order.desc(orderSettings.getPropName());
	}
	
}
