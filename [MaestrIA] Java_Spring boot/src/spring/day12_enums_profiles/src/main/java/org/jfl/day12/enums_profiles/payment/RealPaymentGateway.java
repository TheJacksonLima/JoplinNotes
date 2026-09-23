package org.jfl.day12.enums_profiles.payment;

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

    public void pay(){
        System.out.println("Pay via RealPaymentGateway");
    }
}