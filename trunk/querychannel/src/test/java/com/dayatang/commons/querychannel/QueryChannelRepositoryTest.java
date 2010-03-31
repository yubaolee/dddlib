package com.dayatang.commons.querychannel;

import org.junit.Before;
import org.junit.Test;

import com.dayatang.commons.domain.InstanceFactory;
import com.dayatang.commons.querychannel.domain.MyEntity;
import com.dayatang.commons.querychannel.integration.AbstractIntegratedTestCase;
import com.dayatang.commons.querychannel.service.impl.QueryChannelRepository;
import com.dayatang.commons.querychannel.support.Page;

public class QueryChannelRepositoryTest extends AbstractIntegratedTestCase {

	private QueryChannelRepository repository;

	@Before
	public void setUp() throws Exception {
		repository = InstanceFactory.getInstance(QueryChannelRepository.class);
	}

	// @Test
	public void testQueryPagedByPageNO() {
		Page<MyEntity> page = repository.queryPagedByPageNO(MyEntity.class,
				"from MyEntity", new Object[] {}, 1, 10);

		System.out.println(page.getResult().size());
	}

	@Test
	public void testNamedQuery() {
		Page<MyEntity> page = repository.queryPagedByPageNOAndNamedQuery(
				MyEntity.class, "MyEntity.findByName",
				new Object[] { "%entity%" }, 1, 10);

		System.out.println(page.getResult().size());
	}
}
