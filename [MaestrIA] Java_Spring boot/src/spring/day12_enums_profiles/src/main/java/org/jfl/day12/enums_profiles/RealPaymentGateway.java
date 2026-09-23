package org.jfl.day12.enums_profiles;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@ConditionalOnProperty(
        prefix = "service.kafka",
        name = "enabled",
        havingValue = "true"
)
public class RealPaymentGateway{

    public void pay(){
        System.out.println("Pay via RealPaymentGateway");
    }
}