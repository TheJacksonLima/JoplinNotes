package org.jfl.day12.enums_profiles;

import org.jfl.day12.enums_profiles.payment.PaymentGateway;
import org.springframework.stereotype.Service;

@Service
public class PaymentService{
    private final PaymentGateway gateway;

    public PaymentService(PaymentGateway gateway) {
        this.gateway = gateway;
    }

    public void process(){
        gateway.pay();
    }
}