package org.jfl.domain;

public record InvalidPayment(String reason) implements PaymentFailure{
}
