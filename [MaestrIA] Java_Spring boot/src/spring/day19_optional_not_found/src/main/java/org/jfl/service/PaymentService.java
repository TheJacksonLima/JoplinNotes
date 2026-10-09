package org.jfl.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.domain.PaymentStatus;
import org.jfl.exception.PaymentNotFoundException;
import org.jfl.repository.PaymentRepository;
import org.jfl.strategy.PaymentStrategy;
import org.jfl.utils.PaymentFunctions;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor

public class PaymentService {
    private final Map<String, PaymentStrategy> strategies;
    private final PaymentRepository paymentRepository;

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
        return paymentRepository.findAll();
    }

    public Payment findPaymentById(Long id){
        return paymentRepository.findById(id).orElseThrow( () -> new PaymentNotFoundException(id));
    }


    public List<Payment> findPositivePayments(){
        return paymentRepository.findPositivePayments();
    }

    public List<Payment> findPaymentsByType(PaymentMethod method){
        return paymentRepository.findPaymentsByType(method);
    }
}
