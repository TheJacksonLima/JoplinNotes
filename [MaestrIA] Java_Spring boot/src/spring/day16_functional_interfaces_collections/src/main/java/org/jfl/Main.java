package org.jfl;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.service.PaymentService;
import org.jfl.strategy.PaymentStrategy;
import org.jfl.strategy.StrategyInspector;
import org.jfl.utils.PaymentFunctions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class, args);

        PaymentService paymentService = context.getBean(PaymentService.class);
        PaymentFunctions paymentFunctions = context.getBean(PaymentFunctions.class);
        StrategyInspector inspector = context.getBean(StrategyInspector.class);

        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("100.00"));
        //payment.setMethod(PaymentMethod.PIX);
        payment.setMethod(PaymentMethod.CREDIT_CARD);


        System.out.println(paymentFunctions.isValidAmount(payment));
        System.out.println(paymentFunctions.extractAmount(payment));
        paymentFunctions.logPayment(payment);

        Payment payment1 = paymentFunctions.createPayment(new BigDecimal("100.00"),PaymentMethod.PIX);
        System.out.println(paymentFunctions.isValidAmount(payment1));

        Payment emptyPayment = paymentFunctions.createEmptyPayment();
        System.out.println(emptyPayment);

        inspector.printStrategies();


    }
}