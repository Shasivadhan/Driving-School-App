package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.PaymentRequest;
import com.ksvtech.drivingschool.entity.PaymentMethod;
import com.ksvtech.drivingschool.repository.BatchRepository;
import com.ksvtech.drivingschool.repository.CourseRepository;
import com.ksvtech.drivingschool.repository.StudentRepository;
import com.ksvtech.drivingschool.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/payments")
@RequiredArgsConstructor
public class AdminPaymentController {

    private final PaymentService paymentService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("payments", paymentService.getAll());
        return "admin/payments/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        PaymentRequest form = new PaymentRequest();
        model.addAttribute("payment", form);
        populateReferenceData(model);
        return "admin/payments/form";
    }

    @PostMapping
    public String create(@ModelAttribute("payment") @Valid PaymentRequest form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            populateReferenceData(model);
            return "admin/payments/form";
        }

        paymentService.create(form);
        return "redirect:/admin/payments";
    }

    private void populateReferenceData(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("batches", batchRepository.findAll());
        model.addAttribute("paymentMethods", PaymentMethod.values());
    }
}
