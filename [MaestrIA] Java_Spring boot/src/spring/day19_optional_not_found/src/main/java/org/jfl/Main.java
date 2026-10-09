package org.jfl;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.exception.PaymentNotFoundException;
import org.jfl.utils.PaymentFunctions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/*
Exercise 1
        Optional.of()
Optional.ofNullable()
Optional.empty()

Exercise 2
Optional<Payment>
→ map(Payment::getAmount)

Exercise 3
Optional mapping that returns another Optional
→ compare map() and flatMap()

Exercise 4
orElse()
→ default payment method/string

Exercise 5
orElseGet()
→ use a Supplier that prints when executed
→ observe lazy fallback evaluation

Exercise 6
orElseThrow()
→ intentionally use Optional.empty()
→ verify your custom exception path
*/
@SpringBootApplication
public class Main {

    private static Payment createFallbackPayment() {
        System.out.println("Fallback executed");

        return new Payment(
                BigDecimal.ZERO,
                PaymentMethod.PIX
        );
    }

    private static Optional<String> getPaymentMethod(Payment payment){
        return Optional.ofNullable(payment.getMethod()).map(Enum::name);
    }

    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class, args);

        PaymentFunctions paymentFunctions = context.getBean(PaymentFunctions.class);
        List<Payment> listPayments = new ArrayList<>();

        listPayments.add(paymentFunctions.createPayment(new BigDecimal("100.00"), PaymentMethod.PIX));
        listPayments.add(paymentFunctions.createPayment(new BigDecimal("500.00"), PaymentMethod.PIX));
        listPayments.add(paymentFunctions.createPayment(new BigDecimal("200.00"), PaymentMethod.CREDIT_CARD));
        listPayments.add(paymentFunctions.createPayment(new BigDecimal("1500.00"), PaymentMethod.CREDIT_CARD));
        listPayments.add(paymentFunctions.createPayment(new BigDecimal("-50.00"), PaymentMethod.PIX));

        System.out.println("\nExercise 1:");
        Optional<BigDecimal> paymentOptional = listPayments.stream().map(Payment::getAmount).reduce(BigDecimal::add);
        System.out.println("paymentOptional: "+paymentOptional);

        System.out.println("\nExercise 3:");
        Optional<Payment> p3 = listPayments.stream()
                .filter(p -> p.getAmount()
                        .compareTo(BigDecimal.valueOf(1000)) > 0)
                .findFirst();

        Optional<Optional<String>> methodWithMap = p3.map(Main::getPaymentMethod);
        Optional<String> methodWithFlatMap = p3.flatMap(Main::getPaymentMethod);
        System.out.println(methodWithMap);
        System.out.println(methodWithFlatMap);

        System.out.println("\nExercise 4:");
        Optional<Payment> p4 = listPayments.stream().filter(p -> p.getMethod().name().equals("XXX")).findFirst();
        PaymentMethod pm4 =  p4.map(Payment::getMethod).orElse(PaymentMethod.PIX);
        System.out.println(pm4);

        System.out.println("\nExercise 5:");
        Optional<Payment> existingPayment = listPayments.stream().findFirst();

        Payment result = existingPayment.orElseGet(
                Main::createFallbackPayment
        );

        System.out.println(result);

        System.out.println("\nExercise 6:");
        /*Optional<Payment> missingPayment = Optional.empty();
        Payment payment = missingPayment.orElseThrow(PaymentNotFoundException::new);
        System.out.println(payment);*/
    }

}