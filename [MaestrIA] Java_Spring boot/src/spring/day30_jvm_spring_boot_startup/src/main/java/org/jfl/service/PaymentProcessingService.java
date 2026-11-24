package org.jfl.service;


import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PaymentProcessingService {
    private static final Logger log = LoggerFactory.getLogger(PaymentProcessingService.class);
    public String process(Long paymentId) {

        log.info("Processing payment id {}",paymentId);

        try {
            Thread.sleep(200); //is here so our timing aspect produces an obvious result.
        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt();
        }

        return "APPROVED";
    }
}