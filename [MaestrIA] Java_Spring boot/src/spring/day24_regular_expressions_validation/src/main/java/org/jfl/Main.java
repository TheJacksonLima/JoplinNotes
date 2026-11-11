package org.jfl;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.utils.PaymentBatch;
import org.jfl.utils.PaymentFunctions;
import org.jfl.utils.PaymentSummary;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class, args);

        var cep = Pattern.compile( "\\d{5}-?\\d{3}");
        System.out.println(cep.matcher("80000-000").matches());

        Pattern paymentReference = Pattern.compile("PAY-\\d{4}-\\d{6}");
        boolean ret = paymentReference.matcher("PAY-2026-123456").matches();
        System.out.println(ret);

        Pattern pattern2 = Pattern.compile("\\d{3}");
        Matcher matcher2 = pattern2.matcher("123");
        System.out.println(matcher2.matches());
        System.out.println(pattern2.matcher("ABC-123").matches());

        Pattern pattern3 = Pattern.compile("\\d{3}");
        Matcher matcher3 = pattern3.matcher("ABC123XYZ");
        System.out.println(matcher3.find());

        Pattern pattern4 = Pattern.compile("\\bPIX\\b");
        Matcher matcher4 = pattern4.matcher("Payment method: PIX approve");
        System.out.println(matcher4.find());

        Pattern pattern5 = Pattern.compile("(\\d{5})-(\\d{3})");
        Matcher matcher5 = pattern5.matcher( "80000-000");

        if (matcher5.matches()) {
            System.out.println(matcher5.group(1));
            System.out.println(matcher5.group(2));
        }

        //24. Finding multiple matches
        System.out.println("\n24. Finding multiple matches");
        Pattern pattern6 = Pattern.compile("\\d+");
        Matcher matcher6 = pattern6.matcher( "Payment 123 amount 500");

        while(matcher6.find()) {
            System.out.println(matcher6.group());
        }

        //25. start() and end()
        Matcher matcher7 = pattern6.matcher( "Payment 123 amount 500");
        System.out.println("\n25. start() and end()");
        while (matcher7.find()) {
            System.out.println(matcher7.group());
            System.out.println("start: "+matcher7.start());
            System.out.println("end: "+matcher7.end());
        }




    }

}