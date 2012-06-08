package com.dayatang.security.domain;

import javax.persistence.Entity;


@Entity
public abstract class Assignable extends Grantable {

	private static final long serialVersionUID = -236727933027198641L;

	protected Assignable() {
		super();
	}

	public Assignable(String name) {
		super(name);
	}

}
