package org.jfl.day12.enums_profiles.gateway;

import org.jfl.day12.enums_profiles.domain.Payment;
import org.jfl.day12.enums_profiles.domain.PaymentStatus;

public interface PaymentGateway {
    PaymentStatus pay(Payment payment);
}
