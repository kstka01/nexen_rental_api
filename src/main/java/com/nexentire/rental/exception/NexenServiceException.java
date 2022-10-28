package com.nexentire.rental.exception;

public class NexenServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1001416600254985235L;

	public NexenServiceException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	public NexenServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	public NexenServiceException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
}
