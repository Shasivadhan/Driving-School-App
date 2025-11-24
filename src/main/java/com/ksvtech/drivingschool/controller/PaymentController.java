package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.PaymentRequest;
import com.ksvtech.drivingschool.dto.PaymentResponse;
import com.ksvtech.drivingschool.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse create(@Valid @RequestBody PaymentRequest request) {
        return paymentService.create(request);
    }

    @GetMapping("/{id}")
    public PaymentResponse get(@PathVariable Long id) {
        return paymentService.get(id);
    }

    @GetMapping
    public List<PaymentResponse> getAll() {
        return paymentService.getAll();
    }

    @GetMapping("/student/{studentId}")
    public List<PaymentResponse> getByStudent(@PathVariable Long studentId) {
        return paymentService.getByStudent(studentId);
    }
}
