package com.dayatang.commons.querychannel.infra.tapestry;

import org.apache.tapestry5.ioc.ServiceBinder;

import com.dayatang.commons.querychannel.infra.hibernate.QueryChannelRepositoryImpl;
import com.dayatang.commons.querychannel.service.QueryChannelService;
import com.dayatang.commons.querychannel.service.impl.QueryChannelRepository;
import com.dayatang.commons.querychannel.service.impl.QueryChannelServiceImpl;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class QueryChannelModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(QueryChannelRepository.class,
				QueryChannelRepositoryImpl.class);
		binder.bind(QueryChannelService.class, QueryChannelServiceImpl.class);
	}

//	public QueryChannelServiceImpl buildQueryChannelService() {
//		return new QueryChannelServiceImpl();
//	}

}
