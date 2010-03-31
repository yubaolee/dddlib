package com.dayatang.commons.repository.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.dayatang.commons.domain.Entity;
import com.dayatang.commons.domain.OrderSettings;
import com.dayatang.commons.domain.QueryCriteron;
import com.dayatang.commons.domain.QuerySettings;
import com.dayatang.commons.domain.internal.BetweenCriteron;
import com.dayatang.commons.domain.internal.ContainsTextCriteron;
import com.dayatang.commons.domain.internal.EqCriteron;
import com.dayatang.commons.domain.internal.EqPropCriteron;
import com.dayatang.commons.domain.internal.GeCriteron;
import com.dayatang.commons.domain.internal.GePropCriteron;
import com.dayatang.commons.domain.internal.GtCriteron;
import com.dayatang.commons.domain.internal.GtPropCriteron;
import com.dayatang.commons.domain.internal.InCriteron;
import com.dayatang.commons.domain.internal.IsEmptyCriteron;
import com.dayatang.commons.domain.internal.IsNullCriteron;
import com.dayatang.commons.domain.internal.LeCriteron;
import com.dayatang.commons.domain.internal.LePropCriteron;
import com.dayatang.commons.domain.internal.LtCriteron;
import com.dayatang.commons.domain.internal.LtPropCriteron;
import com.dayatang.commons.domain.internal.NotEmptyCriteron;
import com.dayatang.commons.domain.internal.NotEqCriteron;
import com.dayatang.commons.domain.internal.NotEqPropCriteron;
import com.dayatang.commons.domain.internal.NotNullCriteron;
import com.dayatang.commons.domain.internal.SizeEqCriteron;
import com.dayatang.commons.domain.internal.SizeGeCriteron;
import com.dayatang.commons.domain.internal.SizeGtCriteron;
import com.dayatang.commons.domain.internal.SizeLeCriteron;
import com.dayatang.commons.domain.internal.SizeLtCriteron;
import com.dayatang.commons.domain.internal.SizeNotEqCriteron;
import com.dayatang.commons.domain.internal.StartsWithTextCriteron;

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
		queryString += getWhereClause(settings.getCriterons());
		queryString += getOrderClause(settings.getOrderSettings());
	}

	private String getWhereClause(Set<QueryCriteron> criterons) {
		if (criterons.isEmpty()) {
			return "";
		}
		String result = "";
		for (QueryCriteron criteron : settings.getCriterons()) {
			if (criteron instanceof EqCriteron) {
				result += " and o." + criteron.getPropName() + " = ?";
				params.add(((EqCriteron) criteron).getValue());
			}
			if (criteron instanceof NotEqCriteron) {
				result += " and o." + criteron.getPropName() + " != ?";
				params.add(((NotEqCriteron) criteron).getValue());
			}
			if (criteron instanceof GtCriteron) {
				result += " and o." + criteron.getPropName() + " > ?";
				params.add(((GtCriteron) criteron).getValue());
			}
			if (criteron instanceof GeCriteron) {
				result += " and o." + criteron.getPropName() + " >= ?";
				params.add(((GeCriteron) criteron).getValue());
			}
			if (criteron instanceof LtCriteron) {
				result += " and o." + criteron.getPropName() + " < ?";
				params.add(((LtCriteron) criteron).getValue());
			}
			if (criteron instanceof LeCriteron) {
				result += " and o." + criteron.getPropName() + " <= ?";
				params.add(((LeCriteron) criteron).getValue());
			}
			if (criteron instanceof EqPropCriteron) {
				result += " and o." + criteron.getPropName() + " = o." + ((EqPropCriteron) criteron).getOtherProp();
			}
			if (criteron instanceof NotEqPropCriteron) {
				result += " and o." + criteron.getPropName() + " != o." + ((NotEqPropCriteron) criteron).getOtherProp();
			}
			if (criteron instanceof GtPropCriteron) {
				result += " and o." + criteron.getPropName() + " > o." + ((GtPropCriteron) criteron).getOtherProp();
			}
			if (criteron instanceof GePropCriteron) {
				result += " and o." + criteron.getPropName() + " >= o." + ((GePropCriteron) criteron).getOtherProp();
			}
			if (criteron instanceof LtPropCriteron) {
				result += " and o." + criteron.getPropName() + " < o." + ((LtPropCriteron) criteron).getOtherProp();
			}
			if (criteron instanceof LePropCriteron) {
				result += " and o." + criteron.getPropName() + " <= o." + ((LePropCriteron) criteron).getOtherProp();
			}
			if (criteron instanceof SizeEqCriteron) {
				result += " and size(o." + criteron.getPropName() + ") = ?";
				params.add(((SizeEqCriteron) criteron).getValue());
			}
			if (criteron instanceof SizeNotEqCriteron) {
				result += " and size(o." + criteron.getPropName() + ") != ?";
				params.add(((SizeNotEqCriteron) criteron).getValue());
			}
			if (criteron instanceof SizeGtCriteron) {
				result += " and size(o." + criteron.getPropName() + ") > ?";
				params.add(((SizeGtCriteron) criteron).getValue());
			}
			if (criteron instanceof SizeGeCriteron) {
				result += " and size(o." + criteron.getPropName() + ") >= ?";
				params.add(((SizeGeCriteron) criteron).getValue());
			}
			if (criteron instanceof SizeLtCriteron) {
				result += " and size(o." + criteron.getPropName() + ") < ?";
				params.add(((SizeLtCriteron) criteron).getValue());
			}
			if (criteron instanceof SizeLeCriteron) {
				result += " and size(o." + criteron.getPropName() + ") <= ?";
				params.add(((SizeLeCriteron) criteron).getValue());
			}
			if (criteron instanceof StartsWithTextCriteron) {
				result += " and o." + criteron.getPropName() + " like ?";
				params.add(((StartsWithTextCriteron) criteron).getValue() + "%");
			}
			if (criteron instanceof ContainsTextCriteron) {
				result += " and o." + criteron.getPropName() + " like ?";
				params.add("%" + ((ContainsTextCriteron) criteron).getValue() + "%");
			}
			if (criteron instanceof BetweenCriteron) {
				result += " and o." + criteron.getPropName() + " between ? and ?";
				params.add(((BetweenCriteron) criteron).getFrom());
				params.add(((BetweenCriteron) criteron).getTo());
			}
			if (criteron instanceof InCriteron) {
				Collection<? extends Object> value = ((InCriteron) criteron).getValue();
				if (value.isEmpty()) {
					continue;
				}
				result += " and o." + criteron.getPropName() + " in (" + createInString(value) + ")";
			}
			if (criteron instanceof IsNullCriteron) {
				result += " and o." + criteron.getPropName() + " is null";
			}
			if (criteron instanceof NotNullCriteron) {
				result += " and o." + criteron.getPropName() + " is not null";
			}
			if (criteron instanceof IsEmptyCriteron) {
				result += " and o." + criteron.getPropName() + " is empty";
			}
			if (criteron instanceof NotEmptyCriteron) {
				result += " and o." + criteron.getPropName() + " is not empty";
			}
		}
		result = " where " + result.substring(" and ".length());
		return result;
	}

	private String createInString(Collection<? extends Object> value) {
		String result = "";
		for (Object item : value) {
			if (item instanceof Entity) {
				result += "," + ((Entity)item).getId();
			} else {
				result += "," + item;
			}
		}
		return result.substring(1);
	}

	private String getOrderClause(List<OrderSettings> orderSettings) {
		if (settings.getOrderSettings().isEmpty()) {
			return "";
		}
		String result = "";
		for (OrderSettings orderSetting : settings.getOrderSettings()) {
			result += ", " + orderSetting.getPropName() + (orderSetting.isAscending() ? " asc" : " desc");
		}
		result = " order by " + result.substring(", ".length());
		return result;
	}
}
