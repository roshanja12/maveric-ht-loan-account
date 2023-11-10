package org.banker.loan.exception;

public class ErrorDto {

	private String httpStatus;
	private String errorMessgae;
	private String errorCode;
	public ErrorDto() {
	}

	public ErrorDto(String httpStatus, String errorMessgae, String errorCode) {
		this.httpStatus = httpStatus;
		this.errorMessgae = errorMessgae;
		this.errorCode = errorCode;
	}

	public String getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(String httpStatus) {
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
