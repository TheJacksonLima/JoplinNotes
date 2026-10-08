package org.jfl;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.utils.PaymentFunctions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        System.out.println("\nTotal payment amount");
        BigDecimal  totalAmount = listPayments.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        System.out.println("totalAmount: "+totalAmount);

        System.out.println("\nTotal payment positive amount");
        BigDecimal  totalPositiveAmount = listPayments.stream()
                .map(Payment::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        System.out.println("\ntotalPositiveAmount: "+totalPositiveAmount);

        System.out.println("\ngroupingBy():");
        Map<PaymentMethod, List<Payment>> groupedPayments = listPayments.stream().collect(Collectors.groupingBy(Payment::getMethod));
        System.out.println(groupedPayments.toString());

        System.out.println("\nAmount per Payment Method:");
        //Map<PaymentMethod, BigDecimal> amountPerPayment = listPayments.stream().collect(Collectors.toMap(Payment::getMethod,Payment::getAmount));
        Map<PaymentMethod, BigDecimal> amountPerPayment = listPayments.stream().collect(Collectors.toMap(Payment::getMethod,Payment::getAmount,BigDecimal::add));
        System.out.println(amountPerPayment.toString());

        System.out.println("\nflatMap() — all payments from batches:");

        List<Payment> batch1 = List.of(
                paymentFunctions.createPayment(new BigDecimal("80.00"), PaymentMethod.PIX),
                paymentFunctions.createPayment(new BigDecimal("320.00"), PaymentMethod.CREDIT_CARD)
        );

        List<Payment> batch2 = List.of(
                paymentFunctions.createPayment(new BigDecimal("45.00"), PaymentMethod.PIX),
                paymentFunctions.createPayment(new BigDecimal("990.00"), PaymentMethod.CREDIT_CARD),
                paymentFunctions.createPayment(new BigDecimal("150.00"), PaymentMethod.PIX)
        );

        List<List<Payment>> batches = List.of(batch1, batch2);
        List<Payment>  allBatch = batches.stream().flatMap(List::stream).toList();
        allBatch.forEach(System.out::println);

    }
}