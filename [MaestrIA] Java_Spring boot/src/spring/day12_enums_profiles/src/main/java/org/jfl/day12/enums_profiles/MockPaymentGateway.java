package org.jfl.day12.enums_profiles;

import org.springframework.context.annotation.Profile;

@Profile("dev")
public class MockPaymentGateway {

    public void pay(){
        System.out.println("Pay via MockPaymentGateway");
    }
}