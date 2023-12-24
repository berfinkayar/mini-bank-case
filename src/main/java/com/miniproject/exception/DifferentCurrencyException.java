package com.miniproject.exception;

public class DifferentCurrencyException extends RuntimeException {

    public DifferentCurrencyException() {
        super("Currency types of the source and target accounts do not match");
    }
    
}
