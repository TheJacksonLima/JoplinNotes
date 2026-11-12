package org.jfl.domain;

public record Declined(String reason, int code) implements PaymentResults{ }
