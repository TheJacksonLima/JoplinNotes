package org.jfl.strategy;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentStatus;

@FunctionalInterface
public interface PaymentStrategy {
    PaymentStatus pay(Payment payment);
}
