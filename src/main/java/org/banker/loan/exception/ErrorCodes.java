package org.banker.loan.exception;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class ErrorCodes {

	public static final String INACTIVE_CUSTOMER= "Customer is not Acctive##404";
	public static final String	CONNECTION_ISSUE ="Server Connection Issue ##500";
	public static final String PAGE_NOT_FOUND ="Page not found";



}
