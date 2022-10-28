package com.nexentire.rental.exception;

import java.lang.reflect.Method;

public class NexenUserDefineException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2867588993174554657L;
	
	public NexenUserDefineException() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public NexenUserDefineException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	public NexenUserDefineException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	public NexenUserDefineException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public NexenUserDefineException(Class<?> c, String msg, Throwable cause) {
		
		super(makeMsg(c, msg), cause);
		// TODO Auto-generated constructor stub
	}
	
	public static String makeMsg(Class<?> c, String msg) {
		
		Class<?> _c = c.getClass();
		Method _m = _c.getEnclosingMethod();
		
		String exceptionPath = "[" + _c.getPackage().getName() + "_" + _c.getName() + "_" + _m.getName() + "]";
		
		return exceptionPath + msg;
	}
}
