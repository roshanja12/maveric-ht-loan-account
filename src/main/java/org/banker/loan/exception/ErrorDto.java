package org.banker.loan.exception;

public class ErrorDto {

	private int httpStatus;
	private String errorMessgae;
	private String errorCode;

	public ErrorDto() {
	}

	public ErrorDto(int httpStatus, String errorMessgae, String errorCode) {
		this.httpStatus = httpStatus;
		this.errorMessgae = errorMessgae;
		this.errorCode = errorCode;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getErrorMessgae() {
		return errorMessgae;
	}

	public void setErrorMessgae(String errorMessgae) {
		this.errorMessgae = errorMessgae;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
