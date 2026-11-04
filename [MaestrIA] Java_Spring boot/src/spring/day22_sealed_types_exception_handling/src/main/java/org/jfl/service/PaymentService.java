package org.jfl.service;

import lombok.AllArgsConstructor;
import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.domain.PaymentStatus;
import org.jfl.exception.PaymentNotFoundException;
import org.jfl.repository.PaymentRepository;
import org.jfl.strategy.PaymentStrategy;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


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

    public PaymentStatus processById(Long id){
        Function<Long, Payment> findPayment = this::findPaymentById;
        Function<Payment, PaymentStatus> processPayment = this::process;
        Function<Long,PaymentStatus> findAndProcess = findPayment.andThen(processPayment);

        return findAndProcess.apply(id);

    }
}
