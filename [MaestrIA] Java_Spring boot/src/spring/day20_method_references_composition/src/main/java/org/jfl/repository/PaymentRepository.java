package org.jfl.repository;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.utils.PaymentFunctions;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class PaymentRepository {

    private final Map<Long, Payment> payments = new HashMap<>();

    public PaymentRepository(PaymentFunctions paymentFunctions){
        payments.put(1L,paymentFunctions.createPayment(new BigDecimal("100.00"), PaymentMethod.PIX));
        payments.put(2L,paymentFunctions.createPayment(new BigDecimal("500.00"), PaymentMethod.PIX));
        payments.put(3L,paymentFunctions.createPayment(new BigDecimal("200.00"), PaymentMethod.CREDIT_CARD));
        payments.put(4L,paymentFunctions.createPayment(new BigDecimal("1500.00"), PaymentMethod.CREDIT_CARD));
        payments.put(5L,paymentFunctions.createPayment(new BigDecimal("-50.00"), PaymentMethod.PIX));
    }

    public List<Payment> findAll(){
        return new ArrayList<>(payments.values());
    }

    public Optional<Payment> findById(long id){
       return Optional.ofNullable(payments.get(id));
    }

    public List<Payment> findPositivePayments() {
        return payments.values().stream()
                .filter(PaymentFunctions::isValidAmount)
                .toList();
    }

    public List<Payment> findPaymentsByType(PaymentMethod method) {
        return payments.values().stream()
                .filter(payment ->
                        payment.getMethod() == method
                )
                .toList();
    }
}
