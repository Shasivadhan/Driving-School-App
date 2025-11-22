package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.repository.CertificateRepository;
import com.ksvtech.drivingschool.repository.CourseRepository;
import com.ksvtech.drivingschool.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

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

        // ===== Certificates per course =====
        List<Object[]> byCourse = certificateRepository.countCertificatesByCourse();
        List<String> courseLabels = new ArrayList<>();
        List<Long> courseCounts = new ArrayList<>();
        for (Object[] row : byCourse) {
            courseLabels.add((String) row[0]);
            Number cnt = (Number) row[1];
            courseCounts.add(cnt.longValue());
        }

        // ===== Certificates per month (YYYY-MM) =====
        List<Object[]> byMonth = certificateRepository.countCertificatesPerMonth();
        List<String> monthLabels = new ArrayList<>();
        List<Long> monthCounts = new ArrayList<>();
        for (Object[] row : byMonth) {
            monthLabels.add((String) row[0]);      // e.g. "2025-11"
            Number cnt = (Number) row[1];
            monthCounts.add(cnt.longValue());
        }

        model.addAttribute("certCourseLabels", courseLabels);
        model.addAttribute("certCourseData", courseCounts);
        model.addAttribute("certMonthLabels", monthLabels);
        model.addAttribute("certMonthData", monthCounts);

        // Renders src/main/resources/templates/dashboard.html
        return "dashboard";
    }
}
