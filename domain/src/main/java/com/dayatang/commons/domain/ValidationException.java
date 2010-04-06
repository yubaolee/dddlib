package com.dayatang.commons.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 验证异常
 * @author Yu Yang
 *
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 7766297758555472238L;
	private Set<String> errorCodes = new HashSet<String>();

	public ValidationException() {
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationException(Collection<String> errorCodes) {
		this.errorCodes = new HashSet<String>(errorCodes);
	}

	public Set<String> getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(Collection<String> errorCodes) {
		this.errorCodes = new HashSet<String>(errorCodes);
	}

	public void setErrorCodes(String[] errorCodes) {
		this.errorCodes = new HashSet<String>(Arrays.asList(errorCodes));
	}

}
