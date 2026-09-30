package org.jfl.service;

import org.jfl.config.PaymentStrategyConfig;
import org.jfl.domain.Payment;
import org.jfl.domain.PaymentStatus;
import org.jfl.strategy.PaymentStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {
    private final PaymentStrategy pix;
    private final PaymentStrategy card;

    public PaymentService(@Qualifier("pixStrategy")PaymentStrategy pixStrategy,
                          @Qualifier("cardStrategy")PaymentStrategy cardStrategy){
        this.pix = pixStrategy;
        this.card = cardStrategy;

    }

    public PaymentStatus process(Payment payment){
        PaymentStrategy ps = switch(payment.getMethod()){
                  case PIX -> pix;
                  case CREDIT_CARD -> card;
        };
        return ps.pay(payment);
    }
}
