package org.jfl.controller;

import org.jfl.service.PaymentProcessingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aop")
public class AopDemoController {

    private final PaymentProcessingService paymentProcessingService;

    public AopDemoController(PaymentProcessingService paymentProcessingService) {
        this.paymentProcessingService = paymentProcessingService;
    }

    @GetMapping("/payments/{id}")
    public String process(@PathVariable Long id) {
        return paymentProcessingService.process(id);
    }
}