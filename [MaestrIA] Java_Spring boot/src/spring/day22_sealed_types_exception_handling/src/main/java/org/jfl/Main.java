package org.jfl;

import org.jfl.domain.InvalidPayment;
import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.domain.PaymentMissing;
import org.jfl.mapper.PaymentStatusMapper;
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
        System.out.println(PaymentStatusMapper.message(new PaymentMissing(99L)));
        System.out.println(PaymentStatusMapper.message(new InvalidPayment("Amount must be positive")));
    }

}