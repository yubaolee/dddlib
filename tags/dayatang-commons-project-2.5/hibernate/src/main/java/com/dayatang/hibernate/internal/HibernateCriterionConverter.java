package com.dayatang.hibernate.internal;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

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
public class HibernateCriterionConverter {
	
	@SuppressWarnings("rawtypes")
	public static Criterion convert(QueryCriterion criterion, Criteria criteria) {
		if (criterion instanceof EqCriterion) {
			Property property = Property.forName(((EqCriterion) criterion).getPropName());
					//getPath(((EqCriterion) criterion).getPropName(), criteria);
			System.out.println("------------------------" + property);
			return property.eq(((EqCriterion) criterion).getValue());
		}
		if (criterion instanceof NotEqCriterion) {
			return Restrictions.not(Restrictions.eq(((NotEqCriterion) criterion).getPropName(), ((NotEqCriterion) criterion).getValue()));
		}
		if (criterion instanceof GtCriterion) {
			return Restrictions.gt(((GtCriterion) criterion).getPropName(), ((GtCriterion) criterion).getValue());
		}
		if (criterion instanceof GeCriterion) {
			return Restrictions.ge(((GeCriterion) criterion).getPropName(), ((GeCriterion) criterion).getValue());
		}
		if (criterion instanceof LtCriterion) {
			return Restrictions.lt(((LtCriterion) criterion).getPropName(), ((LtCriterion) criterion).getValue());
		}
		if (criterion instanceof LeCriterion) {
			return Restrictions.le(((LeCriterion) criterion).getPropName(), ((LeCriterion) criterion).getValue());
		}
		if (criterion instanceof EqPropCriterion) {
			return Restrictions.eqProperty(((EqPropCriterion) criterion).getPropName1(), ((EqPropCriterion) criterion).getPropName2());
		}
		if (criterion instanceof NotEqPropCriterion) {
			return Restrictions.not(Restrictions.eqProperty(((NotEqPropCriterion) criterion).getPropName1(), ((NotEqPropCriterion) criterion).getPropName2()));
		}
		if (criterion instanceof GtPropCriterion) {
			return Restrictions.gtProperty(((GtPropCriterion) criterion).getPropName1(), ((GtPropCriterion) criterion).getPropName2());
		}
		if (criterion instanceof GePropCriterion) {
			return Restrictions.geProperty(((GePropCriterion) criterion).getPropName1(), ((GePropCriterion) criterion).getPropName2());
		}
		if (criterion instanceof LtPropCriterion) {
			return Restrictions.ltProperty(((LtPropCriterion) criterion).getPropName1(), ((LtPropCriterion) criterion).getPropName2());
		}
		if (criterion instanceof LePropCriterion) {
			return Restrictions.leProperty(((LePropCriterion) criterion).getPropName1(), ((LePropCriterion) criterion).getPropName2());
		}
		if (criterion instanceof SizeEqCriterion) {
			return Restrictions.sizeEq(((SizeEqCriterion) criterion).getPropName(), ((SizeEqCriterion) criterion).getValue());
		}
		if (criterion instanceof SizeNotEqCriterion) {
			return Restrictions.not(Restrictions.sizeEq(((SizeNotEqCriterion) criterion).getPropName(), ((SizeNotEqCriterion) criterion).getValue()));
		}
		if (criterion instanceof SizeGtCriterion) {
			return Restrictions.sizeGt(((SizeGtCriterion) criterion).getPropName(), ((SizeGtCriterion) criterion).getValue());
		}
		if (criterion instanceof SizeGeCriterion) {
			return Restrictions.sizeGe(((SizeGeCriterion) criterion).getPropName(), ((SizeGeCriterion) criterion).getValue());
		}
		if (criterion instanceof SizeLtCriterion) {
			return Restrictions.sizeLt(((SizeLtCriterion) criterion).getPropName(), ((SizeLtCriterion) criterion).getValue());
		}
		if (criterion instanceof SizeLeCriterion) {
			return Restrictions.sizeLe(((SizeLeCriterion) criterion).getPropName(), ((SizeLeCriterion) criterion).getValue());
		}
		if (criterion instanceof StartsWithTextCriterion) {
			return Restrictions.like(((StartsWithTextCriterion) criterion).getPropName(), ((StartsWithTextCriterion) criterion).getValue(), MatchMode.START);
		}
		if (criterion instanceof ContainsTextCriterion) {
			return Restrictions.like(((ContainsTextCriterion) criterion).getPropName(), ((ContainsTextCriterion) criterion).getValue(), MatchMode.ANYWHERE);
		}
		if (criterion instanceof BetweenCriterion) {
			return Restrictions.between(((BetweenCriterion) criterion).getPropName(), ((BetweenCriterion) criterion).getFrom(), ((BetweenCriterion) criterion).getTo());
		}
		if (criterion instanceof InCriterion) {
			InCriterion inCriterion = (InCriterion) criterion;
			String propName = inCriterion.getPropName();
			Collection<?> value = inCriterion.getValue();
			if (value == null || value.isEmpty()) {
				return Restrictions.idEq(null);
			}
			return Restrictions.in(propName, value);
		}
		if (criterion instanceof NotInCriterion) {
			NotInCriterion notInCriterion = (NotInCriterion) criterion;
			String propName = notInCriterion.getPropName();
			Collection<?> value = notInCriterion.getValue();
			if (value == null || value.isEmpty()) {
				return null;
			}
			return Restrictions.not(Restrictions.in(propName, value));
		}
		if (criterion instanceof IsNullCriterion) {
			return Restrictions.isNull(((IsNullCriterion) criterion).getPropName());
		}
		if (criterion instanceof NotNullCriterion) {
			return Restrictions.isNotNull(((NotNullCriterion) criterion).getPropName());
		}
		if (criterion instanceof IsEmptyCriterion) {
			return Restrictions.isEmpty(((IsEmptyCriterion) criterion).getPropName());
		}
		if (criterion instanceof NotEmptyCriterion) {
			return Restrictions.isNotEmpty(((NotEmptyCriterion) criterion).getPropName());
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

	private static Property getPath(String propName, Criteria criteria) {
		String[] nameExpr = propName.split("\\.");
		if (nameExpr.length < 2) {
			return Property.forName(propName);
		}
		try {
			//criteria.
			criteria.createAlias(nameExpr[0], nameExpr[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Property.forName(propName);
	}

 }
