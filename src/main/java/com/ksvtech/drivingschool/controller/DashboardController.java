package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.DashboardFinanceSummary;
import com.ksvtech.drivingschool.repository.CertificateRepository;
import com.ksvtech.drivingschool.repository.CourseRepository;
import com.ksvtech.drivingschool.repository.StudentRepository;
import com.ksvtech.drivingschool.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CertificateRepository certificateRepository;
    private final PaymentService paymentService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // ===== Basic counts =====
        long studentCount = studentRepository.count();
        long courseCount = courseRepository.count();
        long certificateCount = certificateRepository.count();

        model.addAttribute("studentCount", studentCount);
        model.addAttribute("courseCount", courseCount);
        model.addAttribute("certificateCount", certificateCount);

        // ===== Finance summary (payments) =====
        DashboardFinanceSummary finance = paymentService.getDashboardFinanceSummary();
        model.addAttribute("totalRevenueAllTime", finance.getTotalRevenueAllTime());
        model.addAttribute("totalRevenueThisMonth", finance.getTotalRevenueThisMonth());
        model.addAttribute("totalOutstandingDue", finance.getTotalOutstandingDue());

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
            monthLabels.add((String) row[0]); // e.g. "2025-11"
            Number cnt = (Number) row[1];
            monthCounts.add(cnt.longValue());
        }

        model.addAttribute("certCourseLabels", courseLabels);
        model.addAttribute("certCourseData", courseCounts);
        model.addAttribute("certMonthLabels", monthLabels);
        model.addAttribute("certMonthData", monthCounts);

        // ===== Revenue per month (YYYY-MM) =====
        List<Object[]> revenueRows = paymentService.getMonthlyRevenue();
        List<String> revenueMonthLabels = new ArrayList<>();
        List<BigDecimal> revenueMonthData = new ArrayList<>();

        for (Object[] row : revenueRows) {
            String ym = (String) row[0];              // e.g. "2025-11"
            BigDecimal total = (BigDecimal) row[1];   // monthly sum
            revenueMonthLabels.add(ym);
            revenueMonthData.add(total);
        }

        model.addAttribute("revenueMonthLabels", revenueMonthLabels);
        model.addAttribute("revenueMonthData", revenueMonthData);

        // Renders src/main/resources/templates/dashboard.html
        return "dashboard";
    }
}
