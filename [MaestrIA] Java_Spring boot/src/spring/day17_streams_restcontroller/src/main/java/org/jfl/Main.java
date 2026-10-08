package org.jfl;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.utils.PaymentFunctions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class, args);

        PaymentFunctions paymentFunctions = context.getBean(PaymentFunctions.class);
        List<Payment> listPayments = new ArrayList<>();

        listPayments.add(paymentFunctions.createPayment(new BigDecimal("100.00"), PaymentMethod.PIX));
        listPayments.add(paymentFunctions.createPayment(new BigDecimal("500.00"), PaymentMethod.PIX));
        listPayments.add(paymentFunctions.createPayment(new BigDecimal("200.00"), PaymentMethod.CREDIT_CARD));
        listPayments.add(paymentFunctions.createPayment(new BigDecimal("1500.00"), PaymentMethod.CREDIT_CARD));
        listPayments.add(paymentFunctions.createPayment(new BigDecimal("-50.00"), PaymentMethod.PIX));

        System.out.println("\nPayments with amount > 0:");
        List<Payment> listPositivePayments = listPayments.stream()
                //.filter(p -> p.getAmount().compareTo(BigDecimal.valueOf(0)) > 0)
                .filter(paymentFunctions::isValidAmount)
                .toList();
        listPositivePayments.forEach(System.out::println);

        System.out.println("\nPIX payments:");
        List<Payment> listPixPayments = listPayments.stream()
                .filter(p -> p.getMethod() == PaymentMethod.PIX)
                .toList();
        listPixPayments.forEach(System.out::println);


        System.out.println("\nAmounts ents:");
        List<BigDecimal> amounts = listPayments.stream().map(Payment::getAmount).toList();
        amounts.forEach(System.out::println);

        System.out.println("\nSorted by amount:");
        List<Payment> sortedByAmount = listPayments.stream().sorted(Comparator.comparing(Payment::getAmount)).toList();
        sortedByAmount.forEach(System.out::println);

        System.out.println("\nLaziness:");
        listPayments.stream()
                .filter(payment -> {
                    System.out.println("Filtering " + payment.getAmount());
                    return true;
                });

        listPayments.stream()
                .filter(payment -> {
                    System.out.println("Filtering " + payment.getAmount());
                    return true;
                }).toList();

        System.out.println("\nTake only the first:");
        List<Payment> p2 = listPayments.stream().filter(paymentFunctions::isValidAmount).limit(2).toList();
        p2.forEach(System.out::println);
    }
}