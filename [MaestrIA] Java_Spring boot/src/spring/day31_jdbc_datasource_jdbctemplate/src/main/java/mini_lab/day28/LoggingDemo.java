package mini_lab.day28;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class LoggingDemo {

    private static final Logger log = LoggerFactory.getLogger(LoggingDemo.class);

    public static void main(String[] args) {

        Long paymentId = 42L;
        String reference = "PAY-2026-123456";
        BigDecimal amount = new BigDecimal("150.00");
        String method = "PIX";

        log.trace("Entering payment processing");
        log.debug("Payment reference={} amount={} method={}", reference, amount, method);
        log.info("Payment processed id={} reference={}", paymentId, reference);
        log.warn("Demo warning for payment id={}", paymentId);
    }
}