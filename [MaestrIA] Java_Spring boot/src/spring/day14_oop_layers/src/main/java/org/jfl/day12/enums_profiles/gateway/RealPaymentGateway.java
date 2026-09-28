package org.jfl.day12.enums_profiles.gateway;

import org.jfl.day12.enums_profiles.domain.Payment;
import org.jfl.day12.enums_profiles.domain.PaymentStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@ConditionalOnProperty(
        prefix = "service.kafka",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true // avoids "no PaymentGateway bean" when the property is absent
)
public class RealPaymentGateway implements PaymentGateway {

    @Override
    public PaymentStatus pay(Payment payment) {
        System.out.println("Pay via RealPaymentGateway: " + payment.getValue());
        return PaymentStatus.PENDING;
    }
}
