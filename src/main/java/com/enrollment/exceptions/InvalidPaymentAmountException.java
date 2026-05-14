package com.enrollment.exceptions;

public class InvalidPaymentAmountException extends Exception {
    public InvalidPaymentAmountException(String message) {
        super(message);
    }
}
