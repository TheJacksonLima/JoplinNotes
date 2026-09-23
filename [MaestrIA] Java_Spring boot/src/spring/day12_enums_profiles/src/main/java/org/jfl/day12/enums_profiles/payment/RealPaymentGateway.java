package org.jfl.day12.enums_profiles.payment;

import org.jfl.day12.enums_profiles.PaymentStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@ConditionalOnProperty(
        prefix = "service.kafka",
        name = "enabled",
        havingValue = "true"
)
public class RealPaymentGateway implements  PaymentGateway{

    public PaymentStatus pay(){
        System.out.println("Pay via RealPaymentGateway");
        return PaymentStatus.PENDING;
    }
}