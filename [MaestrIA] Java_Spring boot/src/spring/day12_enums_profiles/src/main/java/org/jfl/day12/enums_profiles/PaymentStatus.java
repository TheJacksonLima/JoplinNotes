package org.jfl.day12.enums_profiles;

public enum PaymentStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELED,
    REFUNDED;

    public String describe(){
        return switch (this){
            case PENDING ->  "Payment pending";
            case APPROVED -> "Payment approved";
            case REJECTED -> "Payment rejected";
            case REFUNDED -> "Payment refunded";
            case CANCELED -> "Payment canceled";
        };
    }
}
