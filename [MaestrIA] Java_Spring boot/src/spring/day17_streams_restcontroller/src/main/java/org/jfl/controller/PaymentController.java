package org.jfl.controller;

import lombok.AllArgsConstructor;
import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @GetMapping
    public List<Payment> getPayments(){
        return service.findAll();
    }

    @GetMapping("/positive")
    public List<Payment> getPositivePayments(){
        return service.findPositivePayments();
    }

    @GetMapping("/type/{method}")
    public List<Payment> getPaymentsByType(@PathVariable PaymentMethod method){
        return service.findPaymentsByType(method);
    }

}
