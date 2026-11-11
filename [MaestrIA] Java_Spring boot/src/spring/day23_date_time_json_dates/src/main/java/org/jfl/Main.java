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

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class, args);

        System.out.println("\nInstant:");
        Instant now = Instant.now();
        Instant later = now.plusSeconds(90);
        Instant earlier = now.minusSeconds(30);

        System.out.println("now:"+now+" later: "+later+ " earlier: "+earlier);
        System.out.println(now.isBefore(later));
        System.out.println(now.isAfter(later));
        System.out.println(now.equals(later));

        System.out.println("\nLocalDate:");
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(1990,5,20);
        LocalDate tomorrow = today.plusDays(1);
        LocalDate yesterday = today.minusDays(1);
        LocalDate nextMonth = today.plusMonths(1);
        LocalDate date = LocalDate.parse("2026-11-10");


        System.out.println(today);
        System.out.println(yesterday);
        System.out.println(birthday);
        System.out.println(tomorrow);
        System.out.println(nextMonth);

        System.out.println("\nLocalDateTime:");
        LocalDateTime now2 = LocalDateTime.now();
        LocalDateTime dateTime =
                LocalDateTime.of(
                        2026,
                        11,
                        10,
                        15,
                        30
                );

        System.out.println(now2);
        System.out.println(dateTime);

        System.out.println("\nZonedDateTime:");
        ZoneId zone = ZoneId.of("America/Sao_Paulo" );
        ZonedDateTime now3 = ZonedDateTime.now(zone);
        System.out.println(now3);

        System.out.println("\nConvert Instant to ZonedDateTime");
        Instant createdAt = Instant.now();
        ZoneId saoPaulo = ZoneId.of("America/Sao_Paulo" );
        ZonedDateTime localTime = createdAt.atZone(saoPaulo);
        System.out.println(localTime);

        System.out.println("\nOne Instant, many local representations:");
        Instant instant =
                Instant.parse(
                        "2026-11-10T14:00:00Z"
                );

        ZonedDateTime brazil =
                instant.atZone(
                        ZoneId.of(
                                "America/Sao_Paulo"
                        )
                );

        ZonedDateTime newYork =
                instant.atZone(
                        ZoneId.of(
                                "America/New_York"
                        )
                );



        System.out.println(now);

        System.out.println(
                Duration.between(
                        now,
                        now.plusSeconds(90)
                ).toSeconds()
        );
    }

}