package org.jfl.service;

import org.jfl.config.PaymentStrategyConfig;
import org.jfl.domain.Payment;
import org.jfl.domain.PaymentStatus;
import org.jfl.strategy.PaymentStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class PaymentService {
    private final Map<String, PaymentStrategy> strategies;

    public PaymentService(Map<String, PaymentStrategy> strategies){
        this.strategies = strategies;

    }

    public PaymentStatus process(Payment payment){

        String strategyName = switch(payment.getMethod()){
                  case PIX -> "pixStrategy";
                  case CREDIT_CARD -> "cardStrategy";
        };


        PaymentStrategy  strategy = strategies.get(strategyName);
        if(strategy == null){
            throw new IllegalArgumentException("Strategy not found: "+strategyName);
        }
        return strategy.pay(payment);
    }
}
