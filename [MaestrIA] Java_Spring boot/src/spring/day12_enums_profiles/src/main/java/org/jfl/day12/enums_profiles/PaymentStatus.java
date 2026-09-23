package org.jfl.day12.enums_profiles;

public enum PaymentStatus {
    PENDING,
    APPROVED,
    REJECTED,
    REFUNDED;

    public String describe(PaymentStatus status){
        return switch (status){
            case PENDING ->  "Payment pending";
            case APPROVED -> "Payment approved";
            case REJECTED -> "Payment rejected";
            case REFUNDED -> "Payment refunded";
        };
    }
}
