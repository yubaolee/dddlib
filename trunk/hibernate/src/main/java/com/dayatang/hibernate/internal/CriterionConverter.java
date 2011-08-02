package com.dayatang.hibernate.internal;

import java.util.Collection;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

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
public class CriterionConverter {
	
	@SuppressWarnings("rawtypes")
	public static Criterion convert(QueryCriterion criterion, DetachedCriteria criteria) {
		if (criterion instanceof EqCriteron) {
			Property property = getPath(((EqCriteron) criterion).getPropName(), criteria);
			System.out.println("------------------------" + property);
			return property.eq(((EqCriteron) criterion).getValue());
		}
		if (criterion instanceof NotEqCriteron) {
			return Restrictions.not(Restrictions.eq(((NotEqCriteron) criterion).getPropName(), ((NotEqCriteron) criterion).getValue()));
		}
		if (criterion instanceof GtCriteron) {
			return Restrictions.gt(((GtCriteron) criterion).getPropName(), ((GtCriteron) criterion).getValue());
		}
		if (criterion instanceof GeCriteron) {
			return Restrictions.ge(((GeCriteron) criterion).getPropName(), ((GeCriteron) criterion).getValue());
		}
		if (criterion instanceof LtCriteron) {
			return Restrictions.lt(((LtCriteron) criterion).getPropName(), ((LtCriteron) criterion).getValue());
		}
		if (criterion instanceof LeCriteron) {
			return Restrictions.le(((LeCriteron) criterion).getPropName(), ((LeCriteron) criterion).getValue());
		}
		if (criterion instanceof EqPropCriteron) {
			return Restrictions.eqProperty(((EqPropCriteron) criterion).getPropName(), ((EqPropCriteron) criterion).getOtherProp());
		}
		if (criterion instanceof NotEqPropCriteron) {
			return Restrictions.not(Restrictions.eqProperty(((NotEqPropCriteron) criterion).getPropName(), ((NotEqPropCriteron) criterion).getOtherProp()));
		}
		if (criterion instanceof GtPropCriteron) {
			return Restrictions.gtProperty(((GtPropCriteron) criterion).getPropName(), ((GtPropCriteron) criterion).getOtherProp());
		}
		if (criterion instanceof GePropCriteron) {
			return Restrictions.geProperty(((GePropCriteron) criterion).getPropName(), ((GePropCriteron) criterion).getOtherProp());
		}
		if (criterion instanceof LtPropCriteron) {
			return Restrictions.ltProperty(((LtPropCriteron) criterion).getPropName(), ((LtPropCriteron) criterion).getOtherProp());
		}
		if (criterion instanceof LePropCriteron) {
			return Restrictions.leProperty(((LePropCriteron) criterion).getPropName(), ((LePropCriteron) criterion).getOtherProp());
		}
		if (criterion instanceof SizeEqCriteron) {
			return Restrictions.sizeEq(((SizeEqCriteron) criterion).getPropName(), ((SizeEqCriteron) criterion).getValue());
		}
		if (criterion instanceof SizeNotEqCriteron) {
			return Restrictions.not(Restrictions.sizeEq(((SizeNotEqCriteron) criterion).getPropName(), ((SizeNotEqCriteron) criterion).getValue()));
		}
		if (criterion instanceof SizeGtCriteron) {
			return Restrictions.sizeGt(((SizeGtCriteron) criterion).getPropName(), ((SizeGtCriteron) criterion).getValue());
		}
		if (criterion instanceof SizeGeCriteron) {
			return Restrictions.sizeGe(((SizeGeCriteron) criterion).getPropName(), ((SizeGeCriteron) criterion).getValue());
		}
		if (criterion instanceof SizeLtCriteron) {
			return Restrictions.sizeLt(((SizeLtCriteron) criterion).getPropName(), ((SizeLtCriteron) criterion).getValue());
		}
		if (criterion instanceof SizeLeCriteron) {
			return Restrictions.sizeLe(((SizeLeCriteron) criterion).getPropName(), ((SizeLeCriteron) criterion).getValue());
		}
		if (criterion instanceof StartsWithTextCriteron) {
			return Restrictions.like(((StartsWithTextCriteron) criterion).getPropName(), ((StartsWithTextCriteron) criterion).getValue(), MatchMode.START);
		}
		if (criterion instanceof ContainsTextCriteron) {
			return Restrictions.like(((ContainsTextCriteron) criterion).getPropName(), ((ContainsTextCriteron) criterion).getValue(), MatchMode.ANYWHERE);
		}
		if (criterion instanceof BetweenCriteron) {
			return Restrictions.between(((BetweenCriteron) criterion).getPropName(), ((BetweenCriteron) criterion).getFrom(), ((BetweenCriteron) criterion).getTo());
		}
		if (criterion instanceof InCriteron) {
			InCriteron inCriteron = (InCriteron) criterion;
			String propName = inCriteron.getPropName();
			Collection<?> value = inCriteron.getValue();
			if (value == null || value.isEmpty()) {
				return Restrictions.idEq(null);
			}
			return Restrictions.in(propName, value);
		}
		if (criterion instanceof NotInCriteron) {
			NotInCriteron notInCriteron = (NotInCriteron) criterion;
			String propName = notInCriteron.getPropName();
			Collection<?> value = notInCriteron.getValue();
			if (value == null || value.isEmpty()) {
				return null;
			}
			return Restrictions.not(Restrictions.in(propName, value));
		}
		if (criterion instanceof IsNullCriteron) {
			return Restrictions.isNull(((IsNullCriteron) criterion).getPropName());
		}
		if (criterion instanceof NotNullCriteron) {
			return Restrictions.isNotNull(((NotNullCriteron) criterion).getPropName());
		}
		if (criterion instanceof IsEmptyCriteron) {
			return Restrictions.isEmpty(((IsEmptyCriteron) criterion).getPropName());
		}
		if (criterion instanceof NotEmptyCriteron) {
			return Restrictions.isNotEmpty(((NotEmptyCriteron) criterion).getPropName());
		}
		if (criterion instanceof NotCriterion) {
			return Restrictions.not(convert(((NotCriterion) criterion).getCriteron(), criteria));
		}
		if (criterion instanceof AndCriterion) {
			AndCriterion andCriterion = (AndCriterion) criterion;
			QueryCriterion[] criterions = andCriterion.getCriterons();
			int length = criterions.length;
			if (length < 2) {
				throw new IllegalArgumentException("AndCriterion params size should >= 2");
			}
			Criterion hibernateCriterion = convert(criterions[0], criteria);
			for (int i = 1; i < length; i++) {
				hibernateCriterion = Restrictions.and(hibernateCriterion, convert(criterions[i], criteria));
			}
			return hibernateCriterion;
		}
		if (criterion instanceof OrCriterion) {
			OrCriterion orCriterion = (OrCriterion) criterion;
			QueryCriterion[] criterions = orCriterion.getCriterons();
			int length = criterions.length;
			if (length < 2) {
				throw new IllegalArgumentException("OrCriterion params size should >= 2");
			}
			Criterion hibernateCriterion = convert(criterions[0], criteria);
			for (int i = 1; i < length; i++) {
				hibernateCriterion = Restrictions.or(hibernateCriterion, convert(criterions[i], criteria));
			}
			return hibernateCriterion;
		}
		return null;
	}

	private static Property getPath(String propName, DetachedCriteria criteria) {
		String[] nameExpr = propName.split("\\.");
		if (nameExpr.length < 2) {
			return Property.forName(propName);
		}
		try {
			criteria.createAlias(nameExpr[0], nameExpr[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Property.forName(propName);
	}

 }
