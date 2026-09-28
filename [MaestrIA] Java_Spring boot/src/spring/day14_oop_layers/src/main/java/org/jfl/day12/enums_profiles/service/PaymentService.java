package org.jfl.day12.enums_profiles.service;

import org.jfl.day12.enums_profiles.domain.Payment;
import org.jfl.day12.enums_profiles.domain.PaymentStatus;
import org.jfl.day12.enums_profiles.gateway.PaymentGateway;
import org.jfl.day12.enums_profiles.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;

    public PaymentService(PaymentRepository paymentRepository, PaymentGateway paymentGateway) {
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
    }

    public Payment process(Payment payment) {
        PaymentStatus status = paymentGateway.pay(payment);
        payment.setStatus(status);
        System.out.println(status.describe());
        return paymentRepository.save(payment);
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }
}
