package org.jfl.utils;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import java.util.function.Function;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Component
public class PaymentFunctions {
        public static final Predicate<Payment>  validAmount = payment -> payment.getAmount().compareTo(BigDecimal.valueOf(0)) > 0;

        private final Function<Payment,BigDecimal> amountExtractor = Payment::getAmount;

        private final Consumer<Payment> paymentLogger = p -> System.out.println(p.getMethod() + ": "+p.getAmount());

        private final BiFunction<BigDecimal,PaymentMethod, Payment> paymentCreator = Payment::new;

        private final Supplier<Payment> paymentSupplier = Payment::new;

        public static boolean isValidAmount(Payment payment) {
             return validAmount.test(payment);
        }

        public BigDecimal extractAmount(Payment payment) {
            return amountExtractor.apply(payment);
        }

        public void logPayment(Payment payment) {
            paymentLogger.accept(payment);
        }

        public Payment createPayment(BigDecimal value, PaymentMethod paymentMethod) {
            return paymentCreator.apply(value,paymentMethod);
        }

        public Payment createEmptyPayment() {
            return paymentSupplier.get();
        }

}
