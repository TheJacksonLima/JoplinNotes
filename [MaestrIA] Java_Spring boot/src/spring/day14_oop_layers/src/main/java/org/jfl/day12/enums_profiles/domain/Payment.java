package org.jfl.day12.enums_profiles.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Payment {
    private Long id;
    private BigDecimal value;
    private PaymentStatus status;
}
