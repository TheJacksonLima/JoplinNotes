package org.jfl.mapper;

import org.jfl.domain.Payment;
import org.jfl.dto.PaymentResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(payment.getAmount(), payment.getMethod().name());
    }

    public List<PaymentResponse> toResponseList(
            List<Payment> payments) {

        return payments.stream()
                .map(this::toResponse)
                .toList();
    }
}