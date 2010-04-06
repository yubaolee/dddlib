package com.dayatang.commons.observer.domain;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unchecked")
public interface Subject extends Serializable {

	public String getSubjectKey();

	public List<Observer> getObservers();

	public void notifyObservers();

}
