package com.dayatang.jpa.internal;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.dayatang.domain.Entity;
import com.dayatang.domain.QueryCriterion;
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
 * 一个工具类,用于将QueryCriterion转换成Hibernate的Criterion
 * @author yyang
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CriterionConverter {

	public static <T extends Entity> Predicate convert(QueryCriterion criterion, CriteriaBuilder builder, Root<T> root, Class<T> entityClass) {
		if (criterion instanceof EqCriteron) {
			Path path = getPath(root, ((EqCriteron) criterion).getPropName());
			return builder.equal(path, ((EqCriteron) criterion).getValue());
		}
		if (criterion instanceof NotEqCriteron) {
			Path path = getPath(root, ((NotEqCriteron) criterion).getPropName());
			return builder.notEqual(path, ((NotEqCriteron) criterion).getValue());
		}
		if (criterion instanceof GtCriteron) {
			GtCriteron theCriteron = (GtCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Number value = theCriteron.getValue();
			return builder.gt(path.as(value.getClass()), value);
		}
		if (criterion instanceof GeCriteron) {
			GeCriteron theCriteron = (GeCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Number value = theCriteron.getValue();
			return builder.ge(path.as(value.getClass()), value);
		}
		if (criterion instanceof LtCriteron) {
			LtCriteron theCriteron = (LtCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Number value = theCriteron.getValue();
			return builder.lt(path.as(value.getClass()), value);
		}
		if (criterion instanceof LeCriteron) {
			LeCriteron theCriteron = (LeCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Number value = theCriteron.getValue();
			return builder.le(path.as(value.getClass()), value);
		}
		if (criterion instanceof EqPropCriteron) {
			EqPropCriteron theCriteron = (EqPropCriteron) criterion;
			Path path1 = getPath(root, theCriteron.getPropName());
			Path path2 = getPath(root, theCriteron.getOtherProp());
			return builder.equal(path1, path2);
		}
		if (criterion instanceof NotEqPropCriteron) {
			NotEqPropCriteron theCriteron = (NotEqPropCriteron) criterion;
			Path path1 = getPath(root, theCriteron.getPropName());
			Path path2 = getPath(root, theCriteron.getOtherProp());
			return builder.notEqual(path1, path2);
		}
		if (criterion instanceof GtPropCriteron) {
			GtPropCriteron theCriteron = (GtPropCriteron) criterion;
			Path path1 = getPath(root, theCriteron.getPropName());
			Path path2 = getPath(root, theCriteron.getOtherProp());
			return builder.greaterThan(path1, path2);
		}
		if (criterion instanceof GePropCriteron) {
			GePropCriteron theCriteron = (GePropCriteron) criterion;
			Path path1 = getPath(root, theCriteron.getPropName());
			Path path2 = getPath(root, theCriteron.getOtherProp());
			return builder.greaterThanOrEqualTo(path1, path2);
		}
		if (criterion instanceof LtPropCriteron) {
			LtPropCriteron theCriteron = (LtPropCriteron) criterion;
			Path path1 = getPath(root, theCriteron.getPropName());
			Path path2 = getPath(root, theCriteron.getOtherProp());
			return builder.lessThan(path1, path2);
		}
		if (criterion instanceof LePropCriteron) {
			LePropCriteron theCriteron = (LePropCriteron) criterion;
			Path path1 = getPath(root, theCriteron.getPropName());
			Path path2 = getPath(root, theCriteron.getOtherProp());
			return builder.lessThanOrEqualTo(path1, path2);
		}
		if (criterion instanceof SizeEqCriteron) {
			SizeEqCriteron theCriteron = (SizeEqCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.equal(builder.size(path), theCriteron.getValue());
		}
		if (criterion instanceof SizeNotEqCriteron) {
			SizeNotEqCriteron theCriteron = (SizeNotEqCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.notEqual(builder.size(path), theCriteron.getValue());
		}
		if (criterion instanceof SizeGtCriteron) {
			SizeGtCriteron theCriteron = (SizeGtCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.gt(builder.size(path), theCriteron.getValue());
		}
		if (criterion instanceof SizeGeCriteron) {
			SizeGeCriteron theCriteron = (SizeGeCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.ge(builder.size(path), theCriteron.getValue());
		}
		if (criterion instanceof SizeLtCriteron) {
			SizeLtCriteron theCriteron = (SizeLtCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.lt(builder.size(path), theCriteron.getValue());
		}
		if (criterion instanceof SizeLeCriteron) {
			SizeLeCriteron theCriteron = (SizeLeCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.le(builder.size(path), theCriteron.getValue());
		}
		if (criterion instanceof StartsWithTextCriteron) {
			StartsWithTextCriteron theCriteron = (StartsWithTextCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.like(path, theCriteron.getValue() + "%");
		}
		if (criterion instanceof ContainsTextCriteron) {
			ContainsTextCriteron theCriteron = (ContainsTextCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.like(path, "%" + theCriteron.getValue() + "%");
		}
		if (criterion instanceof BetweenCriteron) {
			BetweenCriteron theCriteron = (BetweenCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Comparable from = theCriteron.getFrom();
			Comparable to = theCriteron.getTo();
			return builder.between(path, from, to);
		}
		if (criterion instanceof InCriteron) {
			InCriteron theCriteron = (InCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Collection value = theCriteron.getValue();
			if (value.isEmpty()) {
				return builder.le(root.get("id").as(Long.class), -1L);
			}
			return path.in(theCriteron.getValue());
		}
		if (criterion instanceof NotInCriteron) {
			NotInCriteron theCriteron = (NotInCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Collection value = theCriteron.getValue();
			if (value == null || value.isEmpty()) {
				return null;
			}
			return builder.not(path.in(value));
		}
		if (criterion instanceof IsNullCriteron) {
			IsNullCriteron theCriteron = (IsNullCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.isNull(path);
		}
		if (criterion instanceof NotNullCriteron) {
			NotNullCriteron theCriteron = (NotNullCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.isNotNull(path);
		}
		if (criterion instanceof IsEmptyCriteron) {
			IsEmptyCriteron theCriteron = (IsEmptyCriteron) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.isEmpty(path);
		}
		if (criterion instanceof NotEmptyCriteron) {
			NotEmptyCriteron isEmptyCriteron = (NotEmptyCriteron) criterion;
			Path propName = root.get(isEmptyCriteron.getPropName());
			Class propType = propName.getJavaType();
			return builder.isNotEmpty(propName.as(propType));
		}
		if (criterion instanceof NotCriterion) {
			return builder.not(convert(((NotCriterion) criterion).getCriteron(), builder, root, entityClass));
		}
		if (criterion instanceof AndCriterion) {
			AndCriterion andCriterion = (AndCriterion) criterion;
			QueryCriterion[] criterions = andCriterion.getCriterons();
			int length = criterions.length;
			if (length < 2) {
				throw new IllegalArgumentException("AndCriterion params size should >= 2");
			}
			Predicate predicate = convert(criterions[0], builder, root, entityClass);
			for (int i = 1; i < length; i++) {
				predicate = builder.and(predicate, convert(criterions[i], builder, root, entityClass));
			}
			return predicate;
		}
		if (criterion instanceof OrCriterion) {
			OrCriterion orCriterion = (OrCriterion) criterion;
			QueryCriterion[] criterions = orCriterion.getCriterons();
			int length = criterions.length;
			if (length < 2) {
				throw new IllegalArgumentException("OrCriterion params size should >= 2");
			}
			Predicate predicate = convert(criterions[0], builder, root, entityClass);
			for (int i = 1; i < length; i++) {
				predicate = builder.and(predicate, convert(criterions[i], builder, root, entityClass));
			}
			return predicate;
		}
		return null;
	}

	private static Path getPath(Root root, String propName) {
		String[] nameExpr = propName.split("\\.");
		if (nameExpr.length < 2) {
			return root.get(propName);
		}
		Path result = root.get(nameExpr[0]);
		for (int i = 1; i < nameExpr.length; i++) {
			result = result.get(nameExpr[i]);
		}
		return result;
	}

}
