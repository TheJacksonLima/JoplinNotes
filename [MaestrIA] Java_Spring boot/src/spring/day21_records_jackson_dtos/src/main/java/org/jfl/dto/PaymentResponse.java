package org.jfl.dto;


import java.math.BigDecimal;

public record PaymentResponse(BigDecimal amount, String method){ }