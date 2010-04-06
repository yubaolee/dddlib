package com.dayatang.commons.observer.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.commons.domain.AbstractEntity;

@Entity
@Table(name = "commons_observer")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "observer_category", discriminatorType = DiscriminatorType.STRING)
@SuppressWarnings("unchecked")
public abstract class Observer<T extends Subject> extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4841919329150188032L;

	private static final Logger logger = LoggerFactory.getLogger(Observer.class);

	@ElementCollection
	@CollectionTable(name = "commons_observer_subjectkey", joinColumns = @JoinColumn(name = "observer_id"))
	@Column(name = "subject_key")
	private Set<String> subjectKeys = new HashSet<String>();

	public Set<String> getSubjectKeys() {
		return subjectKeys;
	}

	public void setSubjectKeys(Set<String> subjectKeys) {
		this.subjectKeys = subjectKeys;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/*
	 * =======================================
	 * 
	 * action
	 * 
	 * ========================================
	 */

	public abstract void process(T subject);

	public static Observer get(Long id) {
		return AbstractEntity.get(Observer.class, id);
	}

	public static List<Observer> findBySubject(Subject subject) {
		String queryString = "select o from Observer o where :subjectKey in elements(o.subjectKeys))";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectKey", subject.getSubjectKey());
		List<Observer> observers = getRepository().find(queryString, params);

		if (logger.isDebugEnabled()) {
			if (observers.isEmpty()) {
				logger.debug("没有找到一个观察者：subjectKey为【{}】", subject
						.getSubjectKey());
			} else {
				for (Observer observer : observers) {
					logger.debug("找到一个观察者：subjectKey为【{}】，observer为【{}】",
							subject.getSubjectKey(), observer);
				}
			}
		}

		return observers;
	}
}
