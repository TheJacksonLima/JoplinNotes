package org.jfl.dto;

import org.jfl.domain.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(BigDecimal amount, PaymentMethod method) {}
