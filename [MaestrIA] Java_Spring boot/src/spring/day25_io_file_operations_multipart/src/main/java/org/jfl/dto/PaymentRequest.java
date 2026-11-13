package org.jfl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.jfl.domain.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull(
                message =
                        "Amount is required"
        )
        @Positive(
                message =
                        "Amount must be greater than zero"
        )
        BigDecimal amount,

        @NotNull(
                message =
                        "Payment method is required"
        )
        PaymentMethod method,

        @NotBlank(
                message =
                        "Reference is required"
        )
        @Pattern(
                regexp =
                        "PAY-\\d{4}-\\d{6}",
                message =
                        "Reference must follow PAY-YYYY-NNNNNN"
        )
        String reference
        ) {}
