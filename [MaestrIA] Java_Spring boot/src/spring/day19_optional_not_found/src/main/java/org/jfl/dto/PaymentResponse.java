package org.jfl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentResponse {

    private BigDecimal amount;
    private String method;
}