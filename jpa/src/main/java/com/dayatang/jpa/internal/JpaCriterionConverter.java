package com.dayatang.jpa.internal;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.dayatang.domain.Entity;
import com.dayatang.domain.QueryCriterion;
import com.dayatang.domain.internal.AndCriterion;
import com.dayatang.domain.internal.BetweenCriterion;
import com.dayatang.domain.internal.ContainsTextCriterion;
import com.dayatang.domain.internal.EqCriterion;
import com.dayatang.domain.internal.EqPropCriterion;
import com.dayatang.domain.internal.GeCriterion;
import com.dayatang.domain.internal.GePropCriterion;
import com.dayatang.domain.internal.GtCriterion;
import com.dayatang.domain.internal.GtPropCriterion;
import com.dayatang.domain.internal.InCriterion;
import com.dayatang.domain.internal.IsEmptyCriterion;
import com.dayatang.domain.internal.IsNullCriterion;
import com.dayatang.domain.internal.LeCriterion;
import com.dayatang.domain.internal.LePropCriterion;
import com.dayatang.domain.internal.LtCriterion;
import com.dayatang.domain.internal.LtPropCriterion;
import com.dayatang.domain.internal.NotCriterion;
import com.dayatang.domain.internal.NotEmptyCriterion;
import com.dayatang.domain.internal.NotEqCriterion;
import com.dayatang.domain.internal.NotEqPropCriterion;
import com.dayatang.domain.internal.NotInCriterion;
import com.dayatang.domain.internal.NotNullCriterion;
import com.dayatang.domain.internal.OrCriterion;
import com.dayatang.domain.internal.SizeEqCriterion;
import com.dayatang.domain.internal.SizeGeCriterion;
import com.dayatang.domain.internal.SizeGtCriterion;
import com.dayatang.domain.internal.SizeLeCriterion;
import com.dayatang.domain.internal.SizeLtCriterion;
import com.dayatang.domain.internal.SizeNotEqCriterion;
import com.dayatang.domain.internal.StartsWithTextCriterion;

/**
 * 一个工具类,用于将QueryCriterion转换成Hibernate的Criterion
 * @author yyang
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class JpaCriterionConverter {

	public static <T extends Entity> Predicate convert(QueryCriterion criterion, CriteriaBuilder builder, Root<T> root, Class<T> entityClass) {
		if (criterion instanceof EqCriterion) {
			Path path = getPath(root, ((EqCriterion) criterion).getPropName());
			return builder.equal(path, ((EqCriterion) criterion).getValue());
		}
		if (criterion instanceof NotEqCriterion) {
			Path path = getPath(root, ((NotEqCriterion) criterion).getPropName());
			return builder.notEqual(path, ((NotEqCriterion) criterion).getValue());
		}
		if (criterion instanceof GtCriterion) {
			GtCriterion theCriteron = (GtCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Comparable value = theCriteron.getValue();
			return builder.greaterThan(path, value);
		}
		if (criterion instanceof GeCriterion) {
			GeCriterion theCriteron = (GeCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Comparable value = theCriteron.getValue();
			return builder.greaterThanOrEqualTo(path, value);
		}
		if (criterion instanceof LtCriterion) {
			LtCriterion theCriteron = (LtCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Comparable value = theCriteron.getValue();
			return builder.lessThan(path, value);
		}
		if (criterion instanceof LeCriterion) {
			LeCriterion theCriteron = (LeCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Comparable value = theCriteron.getValue();
			return builder.lessThanOrEqualTo(path, value);
		}
		if (criterion instanceof EqPropCriterion) {
			EqPropCriterion theCriteron = (EqPropCriterion) criterion;
			Path path1 = getPath(root, theCriteron.getPropName1());
			Path path2 = getPath(root, theCriteron.getPropName2());
			return builder.equal(path1, path2);
		}
		if (criterion instanceof NotEqPropCriterion) {
			NotEqPropCriterion theCriteron = (NotEqPropCriterion) criterion;
			Path path1 = getPath(root, theCriteron.getPropName1());
			Path path2 = getPath(root, theCriteron.getPropName2());
			return builder.notEqual(path1, path2);
		}
		if (criterion instanceof GtPropCriterion) {
			GtPropCriterion theCriteron = (GtPropCriterion) criterion;
			Path path1 = getPath(root, theCriteron.getPropName1());
			Path path2 = getPath(root, theCriteron.getPropName2());
			return builder.greaterThan(path1, path2);
		}
		if (criterion instanceof GePropCriterion) {
			GePropCriterion theCriteron = (GePropCriterion) criterion;
			Path path1 = getPath(root, theCriteron.getPropName1());
			Path path2 = getPath(root, theCriteron.getPropName2());
			return builder.greaterThanOrEqualTo(path1, path2);
		}
		if (criterion instanceof LtPropCriterion) {
			LtPropCriterion theCriteron = (LtPropCriterion) criterion;
			Path path1 = getPath(root, theCriteron.getPropName1());
			Path path2 = getPath(root, theCriteron.getPropName2());
			return builder.lessThan(path1, path2);
		}
		if (criterion instanceof LePropCriterion) {
			LePropCriterion theCriteron = (LePropCriterion) criterion;
			Path path1 = getPath(root, theCriteron.getPropName1());
			Path path2 = getPath(root, theCriteron.getPropName2());
			return builder.lessThanOrEqualTo(path1, path2);
		}
		if (criterion instanceof SizeEqCriterion) {
			SizeEqCriterion theCriteron = (SizeEqCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.equal(builder.size(path), theCriteron.getValue());
		}
		if (criterion instanceof SizeNotEqCriterion) {
			SizeNotEqCriterion theCriteron = (SizeNotEqCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.notEqual(builder.size(path), theCriteron.getValue());
		}
		if (criterion instanceof SizeGtCriterion) {
			SizeGtCriterion theCriteron = (SizeGtCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.gt(builder.size(path), theCriteron.getValue());
		}
		if (criterion instanceof SizeGeCriterion) {
			SizeGeCriterion theCriteron = (SizeGeCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.ge(builder.size(path), theCriteron.getValue());
		}
		if (criterion instanceof SizeLtCriterion) {
			SizeLtCriterion theCriteron = (SizeLtCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.lt(builder.size(path), theCriteron.getValue());
		}
		if (criterion instanceof SizeLeCriterion) {
			SizeLeCriterion theCriteron = (SizeLeCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.le(builder.size(path), theCriteron.getValue());
		}
		if (criterion instanceof StartsWithTextCriterion) {
			StartsWithTextCriterion theCriteron = (StartsWithTextCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.like(path, theCriteron.getValue() + "%");
		}
		if (criterion instanceof ContainsTextCriterion) {
			ContainsTextCriterion theCriteron = (ContainsTextCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.like(path, "%" + theCriteron.getValue() + "%");
		}
		if (criterion instanceof BetweenCriterion) {
			BetweenCriterion theCriteron = (BetweenCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Comparable from = theCriteron.getFrom();
			Comparable to = theCriteron.getTo();
			return builder.between(path, from, to);
		}
		if (criterion instanceof InCriterion) {
			InCriterion theCriteron = (InCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Collection value = theCriteron.getValue();
			if (value.isEmpty()) {
				return builder.le(root.get("id").as(Long.class), -1L);
			}
			return path.in(theCriteron.getValue());
		}
		if (criterion instanceof NotInCriterion) {
			NotInCriterion theCriteron = (NotInCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			Collection value = theCriteron.getValue();
			if (value == null || value.isEmpty()) {
				return null;
			}
			return builder.not(path.in(value));
		}
		if (criterion instanceof IsNullCriterion) {
			IsNullCriterion theCriteron = (IsNullCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.isNull(path);
		}
		if (criterion instanceof NotNullCriterion) {
			NotNullCriterion theCriteron = (NotNullCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.isNotNull(path);
		}
		if (criterion instanceof IsEmptyCriterion) {
			IsEmptyCriterion theCriteron = (IsEmptyCriterion) criterion;
			Path path = getPath(root, theCriteron.getPropName());
			return builder.isEmpty(path);
		}
		if (criterion instanceof NotEmptyCriterion) {
			NotEmptyCriterion isEmptyCriteron = (NotEmptyCriterion) criterion;
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
