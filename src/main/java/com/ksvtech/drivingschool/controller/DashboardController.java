package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.repository.CertificateRepository;
import com.ksvtech.drivingschool.repository.CourseRepository;
import com.ksvtech.drivingschool.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CertificateRepository certificateRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long studentCount = studentRepository.count();
        long courseCount = courseRepository.count();
        long certificateCount = certificateRepository.count();

        model.addAttribute("studentCount", studentCount);
        model.addAttribute("courseCount", courseCount);
        model.addAttribute("certificateCount", certificateCount);

        return "dashboard"; // dashboard.html
    }
}
