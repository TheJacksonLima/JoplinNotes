package org.jfl.mapper;

import org.jfl.domain.InvalidPayment;
import org.jfl.domain.Payment;
import org.jfl.domain.PaymentFailure;
import org.jfl.domain.PaymentMissing;

public class PaymentStatusMapper {
    static int status(PaymentFailure failure){
        return switch(failure){
            case null -> 500;

            case PaymentMissing missing -> 404;

            case InvalidPayment invalidPayment -> 400;
        };
    }

    public static String message(PaymentFailure failure){
        return switch(failure){
            case PaymentMissing missing -> "Payment not found: "+ missing.id();
            case InvalidPayment invalidPayment -> "Invalid payment: " + invalidPayment.reason();
        };
    }

    public static String describe(
            PaymentFailure failure) {

        return switch (failure) {

            case InvalidPayment invalid
                    when invalid.reason().isBlank() ->
                    "Invalid payment with no reason";

            case InvalidPayment invalid ->
                    "Invalid payment: "
                            + invalid.reason();

            case PaymentMissing missing ->
                    "Payment not found: "
                            + missing.id();
        };
    }
}
