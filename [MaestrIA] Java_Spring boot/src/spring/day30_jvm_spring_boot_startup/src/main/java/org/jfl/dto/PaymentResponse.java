package org.jfl.dto;


import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        BigDecimal amount,
        String method,
        Instant createdAt){ }