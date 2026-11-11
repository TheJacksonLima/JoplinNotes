package org.jfl.utils;

import org.jfl.domain.PaymentMethod;

import java.math.BigDecimal;

public record PaymentSummary(BigDecimal amount, PaymentMethod method) {
    public PaymentSummary{

        if(amount == null){
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if(method == null){
            throw new IllegalArgumentException("Method cannot be null");
        }
    }
}
