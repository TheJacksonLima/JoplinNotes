package org.jfl.day12.enums_profiles.payment;

import org.jfl.day12.enums_profiles.PaymentStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MockPaymentGateway implements PaymentGateway{

    public PaymentStatus pay(){
        System.out.println("Pay via MockPaymentGateway");
        return PaymentStatus.APPROVED;
    }
}