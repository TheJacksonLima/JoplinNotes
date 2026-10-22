package org.jfl.mapper;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.dto.PaymentResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.List;

@Component
public class PaymentMapper {
    private final Function<Payment, PaymentMethod> extractMethod = Payment::getMethod;
    private final Function<Payment, BigDecimal> extractAmount = Payment::getAmount;
    private final Function<PaymentMethod, String> methodName = Enum::name;
    private final Function<Payment, String> paymentResponse = extractMethod.andThen(methodName);


    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(extractAmount.apply(payment),paymentResponse.apply(payment));
    }

    public List<PaymentResponse> toResponseList(
            List<Payment> payments) {

        return payments.stream()
                .map(this::toResponse)
                .toList();
    }
}