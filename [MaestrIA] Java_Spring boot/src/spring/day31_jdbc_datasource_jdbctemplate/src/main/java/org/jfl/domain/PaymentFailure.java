package org.jfl.domain;

public sealed interface PaymentFailure permits PaymentMissing, InvalidPayment{
}
