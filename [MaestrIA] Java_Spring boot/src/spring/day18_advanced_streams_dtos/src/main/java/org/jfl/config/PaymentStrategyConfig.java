package org.jfl.config;

import org.jfl.domain.PaymentMethod;
import org.jfl.domain.PaymentStatus;
import org.jfl.strategy.PaymentStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentStrategyConfig {

    @Bean("pixStrategy")
    public PaymentStrategy pixStrategy(){
        return payment -> {
            System.out.println("Processing PIX");
            return PaymentStatus.APPROVED;
        };
    }

    @Bean("cardStrategy")
    public PaymentStrategy cardStrategy(){
        return payment -> {
            System.out.println("Processing credit card");
            return PaymentStatus.PENDING;
        };
    }

}
