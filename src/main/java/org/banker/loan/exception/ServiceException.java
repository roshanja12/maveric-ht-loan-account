package org.banker.loan.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String code;
	private String message;
	private StackTraceElement[] stackTrace;
	private String exceptionCaughtMessage;
	private String httpStatus;

	public String getMessage() {

		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setHttpStatus(String status) {
		this.httpStatus = status;
	}
	public String getHttpStatus() {
		return httpStatus;
	}
	public ServiceException(String message) {
		super();
		this.message = message;
	}

	public ServiceException(String message, String httpStatus) {
		super();
		this.message = message;
		this.httpStatus = httpStatus;
	}


}
