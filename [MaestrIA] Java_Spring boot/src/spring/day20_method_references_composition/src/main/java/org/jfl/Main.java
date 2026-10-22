package org.jfl;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.utils.PaymentFunctions;
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
        List<Payment> listPayments = new ArrayList<>();

        listPayments.add(paymentFunctions.createPayment(new BigDecimal("100.00"), PaymentMethod.PIX));
        listPayments.add(paymentFunctions.createPayment(new BigDecimal("500.00"), PaymentMethod.PIX));
        listPayments.add(paymentFunctions.createPayment(new BigDecimal("200.00"), PaymentMethod.CREDIT_CARD));
        listPayments.add(paymentFunctions.createPayment(new BigDecimal("1500.00"), PaymentMethod.CREDIT_CARD));
        listPayments.add(paymentFunctions.createPayment(new BigDecimal("-50.00"), PaymentMethod.PIX));

        Payment payment = new Payment(BigDecimal.valueOf(100), PaymentMethod.PIX);
        Function<Payment,BigDecimal> extractAmount =  Payment::getAmount;
        Function<BigDecimal,String> formatAmount =  amount -> "Amount: "+ amount;

        Function<Payment,String> usingAndThen = extractAmount.andThen(formatAmount);
        Function<Payment,String> usingCompose = formatAmount.compose(extractAmount);

        System.out.println(usingAndThen.apply(payment));
        System.out.println(usingCompose.apply(payment));

        Function<Payment,String> processor = extractAmount.andThen(formatAmount);
        List<String> formattedAmounts = listPayments.stream().map(processor).toList();
        formattedAmounts.stream().forEach(System.out::println);

        Function<Payment,PaymentMethod> extractMethod = Payment::getMethod;
        Function<PaymentMethod,String> methodName = Enum::name;
        Function<Payment,String> paymentMethodName = extractMethod.andThen(methodName);
        System.out.println(paymentMethodName.apply(payment));
        List<String> paymentMethods = listPayments.stream().map(paymentMethodName).toList();
        paymentMethods.forEach(System.out::println);


        Function<BigDecimal, BigDecimal> addFee = amount -> amount.add(amount.multiply(new BigDecimal("0.10")));
        Function<Payment, String> processorWithFee = extractAmount.andThen(addFee).andThen(formatAmount);
        System.out.println(processorWithFee.apply(payment));


    }

}