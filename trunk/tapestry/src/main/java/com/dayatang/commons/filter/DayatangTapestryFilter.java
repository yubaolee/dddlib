package com.dayatang.commons.filter;

import javax.servlet.ServletException;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;

import com.dayatang.commons.domain.InstanceFactory;
import com.dayatang.commons.factory.TapestryProvider;

public class DayatangTapestryFilter extends TapestryFilter {

	public void init(Registry registry) throws ServletException {
		super.init(registry);
		InstanceFactory.setInstanceProvider(new TapestryProvider(registry));
	}

}
