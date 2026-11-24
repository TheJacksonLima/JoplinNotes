package org.jfl.utils;

import org.jfl.domain.Payment;
import java.util.List;

public record PaymentBatch(List<PaymentSummary> payments) {
    public PaymentBatch{
        payments = List.copyOf(payments);
    }
}
