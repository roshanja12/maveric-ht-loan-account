package org.banker.loan.exception;

public class AmountNotAvailableException extends RuntimeException {
    public AmountNotAvailableException(String message){
        super(message);
    }
}
