package org.jfl.mapper;

import org.jfl.domain.Approved;
import org.jfl.domain.Declined;
import org.jfl.domain.PaymentResults;

public class PaymentResultMapper {

    public static String describe(PaymentResults result){
        return switch(result){
            case Approved approved -> "Approved:" + approved.tansactionId();
            case Declined declined -> "Declined:" + declined.code() + ":" + declined.reason();

        };
    }
}
