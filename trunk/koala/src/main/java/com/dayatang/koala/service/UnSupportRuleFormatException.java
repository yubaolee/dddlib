package com.dayatang.koala.service;

/**
 * @author chencao
 * 
 */
public class UnSupportRuleFormatException extends RuleRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -777453146281299453L;

	public UnSupportRuleFormatException() {
		super();
	}

	public UnSupportRuleFormatException(String message) {
		super(message);
	}

	public UnSupportRuleFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnSupportRuleFormatException(Throwable cause) {
		super(cause);
	}
}
