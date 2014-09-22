package com.tj.cache;

public class CacheException extends Exception {
	
	private static final long serialVersionUID = 7482716230778789087L;

	public CacheException() {
		super();
	}

	public CacheException(String s) {
		super(s);
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheException(Throwable cause) {
		super(cause);
	}

}
