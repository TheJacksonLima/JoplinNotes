package org.jfl;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.utils.PaymentBatch;
import org.jfl.utils.PaymentFunctions;
import org.jfl.utils.PaymentSummary;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.math.BigDecimal;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class, args);

        PaymentFunctions paymentFunctions = context.getBean(PaymentFunctions.class);

        PaymentSummary summary =
                new PaymentSummary(
                        new BigDecimal("250.00"),
                        PaymentMethod.PIX
                );

        System.out.println(summary);
        System.out.println(summary.amount());
        System.out.println(summary.method());

        PaymentSummary a =
                new PaymentSummary(
                        new BigDecimal("250.00"),
                        PaymentMethod.PIX
                );

        PaymentSummary b =
                new PaymentSummary(
                        new BigDecimal("250.00"),
                        PaymentMethod.PIX
                );

        System.out.println("a.equals(b) "+a.equals(b));
        System.out.println("a == b "+ (a == b));
        System.out.println("a.hashCode() "+a.hashCode());
        System.out.println("b.hashCode() "+b.hashCode());

        //summary.getAmount();
        summary.amount();

        try {
            PaymentSummary c = new PaymentSummary(null, PaymentMethod.PIX);
        }
        catch(IllegalArgumentException e) {
            System.out.println("Invalid PaymentSummary ignored");
        }
        System.out.println("Program continues...");

        List<PaymentSummary> payments = new ArrayList<>();
        payments.add(a);
        PaymentBatch batch = new PaymentBatch(payments);

        System.out.println(batch.payments());
        payments.add(b);
        System.out.println(batch.payments());


    }

}