package org.banker.loan.exception;


import jakarta.persistence.PersistenceException;

public class SQLCustomExceptions extends PersistenceException {

	private static final long serialVersionUID = 1L;

	public SQLCustomExceptions(String msg) {
		super(msg);
	}

}
