package org.jfl.day12.enums_profiles.payment;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MockPaymentGateway implements PaymentGateway{

    public void pay(){
        System.out.println("Pay via MockPaymentGateway");
    }
}