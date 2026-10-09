package org.jfl.controller;

import lombok.AllArgsConstructor;
import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.jfl.dto.PaymentResponse;
import org.jfl.mapper.PaymentMapper;
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
    private final PaymentMapper mapper;

    @GetMapping
    public List<PaymentResponse> getPayments(){
        List<Payment> payments = service.findAll();
        return mapper.toResponseList(payments);
    }

    @GetMapping("/positive")
    public List<PaymentResponse> getPositivePayments(){
        List<Payment> payments = service.findPositivePayments();
        return mapper.toResponseList(payments);

    }

    @GetMapping("/type/{method}")
    public List<PaymentResponse> getPaymentsByType(@PathVariable PaymentMethod method){
        List<Payment> payments = service.findPaymentsByType(method);
        return mapper.toResponseList(payments);
    }

    @GetMapping("/{id}")
    public PaymentResponse getPaymentById(
            @PathVariable Long id) {

        Payment payment = service.findPaymentById(id);

        return mapper.toResponse(payment);
    }

}
