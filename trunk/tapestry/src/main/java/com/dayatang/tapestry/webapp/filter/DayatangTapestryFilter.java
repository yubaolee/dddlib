package com.dayatang.tapestry.webapp.filter;

import javax.servlet.ServletException;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.tapestry.factory.TapestryProvider;

public class DayatangTapestryFilter extends TapestryFilter {

	public void init(Registry registry) throws ServletException {
		super.init(registry);
		InstanceFactory.setInstanceProvider(new TapestryProvider(registry));
	}

}
