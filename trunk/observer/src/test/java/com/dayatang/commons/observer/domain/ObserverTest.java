package com.dayatang.commons.observer.domain;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.dayatang.commons.observer.integration.AbstractIntegratedTestCase;

public class ObserverTest extends AbstractIntegratedTestCase {

	@Test
	public void process() {
		Baby baby = new Baby();
		baby.cry();

		MotherObserver observer_1 = (MotherObserver) Observer.get(1L);
		assertTrue(observer_1.getBuyFood());
		FatherObserver observer_2 = (FatherObserver) Observer.get(2L);
		assertTrue(observer_2.getStartCar());
	}

	@Test
	public void getKeys() {
		MotherObserver observer_1 = (MotherObserver) Observer.get(1L);
		Set<String> keys = observer_1.getSubjectKeys();

		assertEquals("BABY-SUBJECT", keys.iterator().next());

		keys = new HashSet<String>();
		keys.add("1");
		observer_1.setSubjectKeys(keys);
		observer_1.save();

		keys = observer_1.getSubjectKeys();

		assertEquals("1", keys.iterator().next());

	}
}
