package com.nexentire.rental.exception;

import org.springframework.dao.DataAccessException;

public class NexenDaoException extends DataAccessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5688189596811171318L;

	public NexenDaoException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	public NexenDaoException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
}
