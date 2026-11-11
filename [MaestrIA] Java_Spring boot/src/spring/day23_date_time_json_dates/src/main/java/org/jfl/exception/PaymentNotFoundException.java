package org.jfl.exception;


public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException() {
        super("Payment not found");
    }

    public PaymentNotFoundException(Long id) {
        super("Payment not found: "+id);
    }
}