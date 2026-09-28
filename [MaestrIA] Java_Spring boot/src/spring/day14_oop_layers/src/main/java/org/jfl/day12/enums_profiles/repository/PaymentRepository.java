package org.jfl.day12.enums_profiles.repository;

import org.jfl.day12.enums_profiles.domain.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PaymentRepository {
    private final Map<Long, Payment> payments = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong();

    public Payment save(Payment payment) {
        if (payment.getId() == null) {
            payment.setId(idSequence.incrementAndGet());
        }
        payments.put(payment.getId(), payment);
        System.out.println("Saving payment: " + payment);
        return payment;
    }

    public Optional<Payment> findById(Long id) {
        return Optional.ofNullable(payments.get(id));
    }

    public List<Payment> findAll() {
        return new ArrayList<>(payments.values());
    }

    public long count() {
        return payments.size();
    }
}
