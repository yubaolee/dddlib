package com.dayatang.i18n;

public class NoSuchMessageException extends RuntimeException {

	private static final long serialVersionUID = 4112592162341621064L;

	public NoSuchMessageException() {
		super();
	}

	public NoSuchMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchMessageException(String message) {
		super(message);
	}

	public NoSuchMessageException(Throwable cause) {
		super(cause);
	}

}
