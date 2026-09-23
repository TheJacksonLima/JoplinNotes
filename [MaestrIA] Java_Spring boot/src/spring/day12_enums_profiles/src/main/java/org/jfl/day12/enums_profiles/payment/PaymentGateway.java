package org.jfl.day12.enums_profiles.payment;

import org.jfl.day12.enums_profiles.PaymentStatus;

public interface PaymentGateway{
    PaymentStatus pay();
}