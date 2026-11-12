package org.jfl.dto;
import java.time.Instant;

public record PaymentTimeRequest(
        Instant processedAt
) {
}