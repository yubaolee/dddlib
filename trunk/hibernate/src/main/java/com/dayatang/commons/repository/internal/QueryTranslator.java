package com.dayatang.commons.repository.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

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
		List<String> elements = new ArrayList<String>();
		for (QueryCriteron criteron : settings.getCriterons()) {
			if (criteron instanceof EqCriteron) {
				elements.add("o." + criteron.getPropName() + " = ?");
				params.add(((EqCriteron) criteron).getValue());
			}
			if (criteron instanceof NotEqCriteron) {
				elements.add("o." + criteron.getPropName() + " != ?");
				params.add(((NotEqCriteron) criteron).getValue());
			}
			if (criteron instanceof GtCriteron) {
				elements.add("o." + criteron.getPropName() + " > ?");
				params.add(((GtCriteron) criteron).getValue());
			}
			if (criteron instanceof GeCriteron) {
				elements.add("o." + criteron.getPropName() + " >= ?");
				params.add(((GeCriteron) criteron).getValue());
			}
			if (criteron instanceof LtCriteron) {
				elements.add("o." + criteron.getPropName() + " < ?");
				params.add(((LtCriteron) criteron).getValue());
			}
			if (criteron instanceof LeCriteron) {
				elements.add("o." + criteron.getPropName() + " <= ?");
				params.add(((LeCriteron) criteron).getValue());
			}
			if (criteron instanceof EqPropCriteron) {
				elements.add("o." + criteron.getPropName() + " = o." + ((EqPropCriteron) criteron).getOtherProp());
			}
			if (criteron instanceof NotEqPropCriteron) {
				elements.add("o." + criteron.getPropName() + " != o." + ((NotEqPropCriteron) criteron).getOtherProp());
			}
			if (criteron instanceof GtPropCriteron) {
				elements.add("o." + criteron.getPropName() + " > o." + ((GtPropCriteron) criteron).getOtherProp());
			}
			if (criteron instanceof GePropCriteron) {
				elements.add("o." + criteron.getPropName() + " >= o." + ((GePropCriteron) criteron).getOtherProp());
			}
			if (criteron instanceof LtPropCriteron) {
				elements.add("o." + criteron.getPropName() + " < o." + ((LtPropCriteron) criteron).getOtherProp());
			}
			if (criteron instanceof LePropCriteron) {
				elements.add("o." + criteron.getPropName() + " <= o." + ((LePropCriteron) criteron).getOtherProp());
			}
			if (criteron instanceof SizeEqCriteron) {
				elements.add("size(o." + criteron.getPropName() + ") = ?");
				params.add(((SizeEqCriteron) criteron).getValue());
			}
			if (criteron instanceof SizeNotEqCriteron) {
				elements.add("size(o." + criteron.getPropName() + ") != ?");
				params.add(((SizeNotEqCriteron) criteron).getValue());
			}
			if (criteron instanceof SizeGtCriteron) {
				elements.add("size(o." + criteron.getPropName() + ") > ?");
				params.add(((SizeGtCriteron) criteron).getValue());
			}
			if (criteron instanceof SizeGeCriteron) {
				elements.add("size(o." + criteron.getPropName() + ") >= ?");
				params.add(((SizeGeCriteron) criteron).getValue());
			}
			if (criteron instanceof SizeLtCriteron) {
				elements.add("size(o." + criteron.getPropName() + ") < ?");
				params.add(((SizeLtCriteron) criteron).getValue());
			}
			if (criteron instanceof SizeLeCriteron) {
				elements.add("size(o." + criteron.getPropName() + ") <= ?");
				params.add(((SizeLeCriteron) criteron).getValue());
			}
			if (criteron instanceof StartsWithTextCriteron) {
				elements.add("o." + criteron.getPropName() + " like ?");
				params.add(((StartsWithTextCriteron) criteron).getValue() + "%");
			}
			if (criteron instanceof ContainsTextCriteron) {
				elements.add("o." + criteron.getPropName() + " like ?");
				params.add("%" + ((ContainsTextCriteron) criteron).getValue() + "%");
			}
			if (criteron instanceof BetweenCriteron) {
				elements.add("o." + criteron.getPropName() + " between ? and ?");
				params.add(((BetweenCriteron) criteron).getFrom());
				params.add(((BetweenCriteron) criteron).getTo());
			}
			if (criteron instanceof InCriteron) {
				Collection<? extends Object> value = ((InCriteron) criteron).getValue();
				elements.add("o." + criteron.getPropName() + " in (" + createInString(value) + ")");
			}
			if (criteron instanceof IsNullCriteron) {
				elements.add("o." + criteron.getPropName() + " is null");
			}
			if (criteron instanceof NotNullCriteron) {
				elements.add("o." + criteron.getPropName() + " is not null");
			}
			if (criteron instanceof IsEmptyCriteron) {
				elements.add("o." + criteron.getPropName() + " is empty");
			}
			if (criteron instanceof NotEmptyCriteron) {
				elements.add("o." + criteron.getPropName() + " is not empty");
			}
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

	private String getOrderClause(List<OrderSettings> orderSettings) {
		if (orderSettings == null || orderSettings.isEmpty()) {
			return "";
		}
		List<String> elements = new ArrayList<String>();
		for (OrderSettings orderSetting : orderSettings) {
			elements.add(orderSetting.getPropName() + (orderSetting.isAscending() ? " asc" : " desc"));
		}
		return " order by " + StringUtils.join(elements, ",");
	}
}
