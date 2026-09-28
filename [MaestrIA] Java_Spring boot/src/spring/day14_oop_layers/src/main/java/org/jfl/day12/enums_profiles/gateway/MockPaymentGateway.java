package org.jfl.day12.enums_profiles.gateway;

import org.jfl.day12.enums_profiles.domain.Payment;
import org.jfl.day12.enums_profiles.domain.PaymentStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("dev")
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public PaymentStatus pay(Payment payment) {
        System.out.println("Pay via MockPaymentGateway: " + payment.getValue());
        if (payment.getValue() == null || payment.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            return PaymentStatus.REJECTED;
        }
        return PaymentStatus.APPROVED;
    }
}
