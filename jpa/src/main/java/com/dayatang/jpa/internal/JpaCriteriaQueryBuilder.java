package com.dayatang.jpa.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.dayatang.domain.Entity;
import com.dayatang.domain.OrderSettings;
import com.dayatang.domain.QueryCriterion;
import com.dayatang.domain.QuerySettings;

public class JpaCriteriaQueryBuilder {

	public static final  <T extends Entity> CriteriaQuery<T> createCriteriaQuery(QuerySettings<T> settings, EntityManager entityManager) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(settings.getEntityClass());
		Root<T> root = query.from(settings.getEntityClass());
		query.select(root);
		for (Map.Entry<String, String> aliasEntry : settings.getAliases().entrySet()) {
			root.get(aliasEntry.getKey()).alias(aliasEntry.getValue());
		}
		
		List<Predicate> criterions = new ArrayList<Predicate>();
		for (QueryCriterion criterion : settings.getCriterions()) {
			Predicate predicate = JpaCriterionConverter.convert(criterion, builder, root, settings.getEntityClass());
			if (predicate != null) {
				criterions.add(predicate);
			}
		}
		if (criterions.size() == 1) {
			query.where(criterions.get(0));
		}
		if (criterions.size() > 1) {
			query.where(criterions.toArray(new Predicate[0]));
		}
		query.orderBy(toOrder(builder, root, settings.getOrderSettings()));
		return query;
	}

	@SuppressWarnings("rawtypes")
	private static Order[] toOrder(CriteriaBuilder builder, Root root, List<OrderSettings> orderSettings) {
		Order[] results = new Order[orderSettings.size()];
		int i = 0;
		for (OrderSettings orderSetting : orderSettings) {
			if (orderSetting.isAscending()) {
				results[i] = builder.asc(root.get(orderSetting.getPropName()));
			} else {
				results[i] = builder.desc(root.get(orderSetting.getPropName()));
			}
			i++;
		}
		return results;
	}
}
