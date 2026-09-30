package org.jfl;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.service.PaymentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class, args);

        PaymentService paymentService = context.getBean(PaymentService.class);

        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("100.00"));
        //payment.setMethod(PaymentMethod.PIX);
        payment.setMethod(PaymentMethod.CREDIT_CARD);

        var status = paymentService.process(payment);

        System.out.println(status);
    }
}