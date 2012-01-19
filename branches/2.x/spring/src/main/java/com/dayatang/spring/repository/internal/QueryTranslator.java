package com.dayatang.spring.repository.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.dayatang.domain.*;
import com.dayatang.domain.internal.*;

public class QueryTranslator {

	private QuerySettings<? extends Entity> settings;

	private String queryString = "";

	private List<Object> params = new ArrayList<Object>();

	public String getQueryString() {
		return queryString;
	}

	public List<Object> getParams() {
		return params;
	}

	public QueryTranslator(QuerySettings<? extends Entity> settings) {
		this.settings = settings;
		prepare();
	}

	private void prepare() {
		queryString = "select distinct(o) from " + settings.getEntityClass().getName() + " as o ";
		queryString += getWhereClause(settings.getCriterions());
		queryString += getOrderClause(settings.getOrderSettings());
	}

	private String getWhereClause(Set<QueryCriterion> criterions) {
		if (criterions.isEmpty()) {
			return "";
		}
		List<String> elements = new ArrayList<String>();
		for (QueryCriterion criterion : settings.getCriterions()) {
			if (criterion instanceof EqCriterion) {
				elements.add("o." + ((EqCriterion)criterion).getPropName() + " = ?");
				params.add(((EqCriterion) criterion).getValue());
			}
			if (criterion instanceof NotEqCriterion) {
				elements.add("o." + ((NotEqCriterion) criterion).getPropName() + " != ?");
				params.add(((NotEqCriterion) criterion).getValue());
			}
			if (criterion instanceof GtCriterion) {
				elements.add("o." + ((GtCriterion) criterion).getPropName() + " > ?");
				params.add(((GtCriterion) criterion).getValue());
			}
			if (criterion instanceof GeCriterion) {
				elements.add("o." + ((GeCriterion) criterion).getPropName() + " >= ?");
				params.add(((GeCriterion) criterion).getValue());
			}
			if (criterion instanceof LtCriterion) {
				elements.add("o." + ((LtCriterion) criterion).getPropName() + " < ?");
				params.add(((LtCriterion) criterion).getValue());
			}
			if (criterion instanceof LeCriterion) {
				elements.add("o." + ((LeCriterion) criterion).getPropName() + " <= ?");
				params.add(((LeCriterion) criterion).getValue());
			}
			if (criterion instanceof EqPropCriterion) {
				elements.add("o." + ((EqPropCriterion) criterion).getPropName1() + " = o." + ((EqPropCriterion) criterion).getPropName2());
			}
			if (criterion instanceof NotEqPropCriterion) {
				elements.add("o." + ((NotEqPropCriterion) criterion).getPropName1() + " != o." + ((NotEqPropCriterion) criterion).getPropName2());
			}
			if (criterion instanceof GtPropCriterion) {
				elements.add("o." + ((GtPropCriterion) criterion).getPropName1() + " > o." + ((GtPropCriterion) criterion).getPropName2());
			}
			if (criterion instanceof GePropCriterion) {
				elements.add("o." + ((GePropCriterion) criterion).getPropName1() + " >= o." + ((GePropCriterion) criterion).getPropName2());
			}
			if (criterion instanceof LtPropCriterion) {
				elements.add("o." + ((LtPropCriterion) criterion).getPropName1() + " < o." + ((LtPropCriterion) criterion).getPropName2());
			}
			if (criterion instanceof LePropCriterion) {
				elements.add("o." + ((LePropCriterion) criterion).getPropName1() + " <= o." + ((LePropCriterion) criterion).getPropName2());
			}
			if (criterion instanceof SizeEqCriterion) {
				elements.add("size(o." + ((SizeEqCriterion) criterion).getPropName() + ") = ?");
				params.add(((SizeEqCriterion) criterion).getValue());
			}
			if (criterion instanceof SizeNotEqCriterion) {
				elements.add("size(o." + ((SizeNotEqCriterion) criterion).getPropName() + ") != ?");
				params.add(((SizeNotEqCriterion) criterion).getValue());
			}
			if (criterion instanceof SizeGtCriterion) {
				elements.add("size(o." + ((SizeGtCriterion) criterion).getPropName() + ") > ?");
				params.add(((SizeGtCriterion) criterion).getValue());
			}
			if (criterion instanceof SizeGeCriterion) {
				elements.add("size(o." + ((SizeGeCriterion) criterion).getPropName() + ") >= ?");
				params.add(((SizeGeCriterion) criterion).getValue());
			}
			if (criterion instanceof SizeLtCriterion) {
				elements.add("size(o." + ((SizeLtCriterion) criterion).getPropName() + ") < ?");
				params.add(((SizeLtCriterion) criterion).getValue());
			}
			if (criterion instanceof SizeLeCriterion) {
				elements.add("size(o." + ((SizeLeCriterion) criterion).getPropName() + ") <= ?");
				params.add(((SizeLeCriterion) criterion).getValue());
			}
			if (criterion instanceof StartsWithTextCriterion) {
				elements.add("o." + ((StartsWithTextCriterion) criterion).getPropName() + " like ?");
				params.add(((StartsWithTextCriterion) criterion).getValue() + "%");
			}
			if (criterion instanceof ContainsTextCriterion) {
				elements.add("o." + ((ContainsTextCriterion) criterion).getPropName() + " like ?");
				params.add("%" + ((ContainsTextCriterion) criterion).getValue() + "%");
			}
			if (criterion instanceof BetweenCriterion) {
				elements.add("o." + ((BetweenCriterion) criterion).getPropName() + " between ? and ?");
				params.add(((BetweenCriterion) criterion).getFrom());
				params.add(((BetweenCriterion) criterion).getTo());
			}
			if (criterion instanceof InCriterion) {
				Collection<? extends Object> value = ((InCriterion) criterion).getValue();
				if (value == null || value.isEmpty()) {
					elements.add("1 > 1");
				} else {
					elements.add("o." + ((InCriterion) criterion).getPropName() + " in (" + createInString(value) + ")");
				}
			}
			if (criterion instanceof NotInCriterion) {
				Collection<? extends Object> value = ((NotInCriterion) criterion).getValue();
				if (value == null || value.isEmpty()) {
					continue;
				}
				elements.add("o." + ((NotInCriterion) criterion).getPropName() + " not in (" + createInString(value) + ")");
			}
			if (criterion instanceof IsNullCriterion) {
				elements.add("o." + ((IsNullCriterion) criterion).getPropName() + " is null");
			}
			if (criterion instanceof NotNullCriterion) {
				elements.add("o." + ((NotNullCriterion) criterion).getPropName() + " is not null");
			}
			if (criterion instanceof IsEmptyCriterion) {
				elements.add("o." + ((IsEmptyCriterion)criterion).getPropName() + " is empty");
			}
			if (criterion instanceof NotEmptyCriterion) {
				elements.add("o." + ((NotEmptyCriterion)criterion).getPropName() + " is not empty");
			}
		}
		if (elements.isEmpty()) {
			return "";
		}
		return " where " +  StringUtils.join(elements, " and ");
	}

	private String createInString(Collection<? extends Object> value) {
		Set<Object> elements = new HashSet<Object>();
		for (Object item : value) {
			Object element;
			if (item instanceof Entity) {
				element = ((Entity)item).getId();
			} else {
				element = item;
			}
			if (element instanceof String || element instanceof Date) {
				element = "'" + element + "'";
			}
			elements.add(element);
		}
		return StringUtils.join(elements, ",");
	}

	private String getOrderClause(List<OrderSetting> OrderSetting) {
		if (OrderSetting == null || OrderSetting.isEmpty()) {
			return "";
		}
		List<String> elements = new ArrayList<String>();
		for (OrderSetting orderSetting : OrderSetting) {
			elements.add(orderSetting.getPropName() + (orderSetting.isAscending() ? " asc" : " desc"));
		}
		return " order by " + StringUtils.join(elements, ",");
	}
}
