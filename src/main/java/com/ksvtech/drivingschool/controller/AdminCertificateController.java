package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.CertificateRequest;
import com.ksvtech.drivingschool.dto.CertificateResponse;
import com.ksvtech.drivingschool.repository.CourseRepository;
import com.ksvtech.drivingschool.repository.StudentRepository;
import com.ksvtech.drivingschool.service.CertificateService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/certificates")
@RequiredArgsConstructor
@Validated
public class AdminCertificateController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CertificateService certificateService;

    @GetMapping("/new")
    public String showGenerateForm(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        return "admin/certificates/form";
    }

    @PostMapping
    public String generateCertificate(@RequestParam Long studentId,
                                      @RequestParam Long courseId,
                                      @RequestParam
                                      @Size(min = 12, max = 12, message = "Aadhaar must be 12 digits")
                                      String aadharNumber,
                                      RedirectAttributes redirectAttributes) {

        CertificateRequest request = new CertificateRequest();
        request.setStudentId(studentId);
        request.setCourseId(courseId);
        request.setAadharNumber(aadharNumber);

        CertificateResponse response = certificateService.createCertificate(request);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Certificate generated: " + response.getCertificateNumber()
        );
        redirectAttributes.addFlashAttribute(
                "pdfLink",
                "/api/certificates/" + response.getId() + "/pdf"
        );

        return "redirect:/admin/certificates/new";
    }
}
