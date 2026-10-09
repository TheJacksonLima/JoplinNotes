package org.jfl.service;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.domain.PaymentStatus;
import org.jfl.strategy.PaymentStrategy;
import org.jfl.utils.PaymentFunctions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class PaymentService {
    private final Map<String, PaymentStrategy> strategies;
    private final List<Payment> payments = new ArrayList<>();
    private final PaymentFunctions paymentFunctions;


    public PaymentService(Map<String, PaymentStrategy> strategies, PaymentFunctions paymentFunctions){
        this.strategies = strategies;
        this.paymentFunctions = paymentFunctions;

        payments.add(paymentFunctions.createPayment(new BigDecimal("100.00"), PaymentMethod.PIX));
        payments.add(paymentFunctions.createPayment(new BigDecimal("500.00"), PaymentMethod.PIX));
        payments.add(paymentFunctions.createPayment(new BigDecimal("200.00"), PaymentMethod.CREDIT_CARD));
        payments.add(paymentFunctions.createPayment(new BigDecimal("1500.00"), PaymentMethod.CREDIT_CARD));
        payments.add(paymentFunctions.createPayment(new BigDecimal("-50.00"), PaymentMethod.PIX));

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

    public List<Payment> findAll(){
        return List.copyOf(payments);
    }

    public List<Payment> findPositivePayments(){
        return payments.stream().filter(paymentFunctions::isValidAmount).toList();
    }

    public List<Payment> findPaymentsByType(PaymentMethod method){
        return payments.stream().filter(p -> p.getMethod() == method).toList();
    }
}
