package com.ksvtech.drivingschool.service;

import com.ksvtech.drivingschool.dto.PaymentRequest;
import com.ksvtech.drivingschool.dto.PaymentResponse;
import com.ksvtech.drivingschool.entity.*;
import com.ksvtech.drivingschool.exception.BusinessException;
import com.ksvtech.drivingschool.exception.NotFoundException;
import com.ksvtech.drivingschool.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ksvtech.drivingschool.dto.DashboardFinanceSummary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;

    @Transactional
    public PaymentResponse create(PaymentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        Batch batch = null;
        if (request.getBatchId() != null) {
            batch = batchRepository.findById(request.getBatchId())
                    .orElseThrow(() -> new NotFoundException("Batch not found"));
        }

        BigDecimal totalFee = request.getTotalFee();
        BigDecimal amount = request.getAmount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Payment amount must be positive");
        }
        if (totalFee.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Total fee must be positive");
        }

        BigDecimal alreadyPaid =
                paymentRepository.getTotalPaidForStudentCourse(student.getId(), course.getId());
        BigDecimal paidAfter = alreadyPaid.add(amount);
        BigDecimal due = totalFee.subtract(paidAfter);

        if (due.compareTo(BigDecimal.ZERO) < 0) {
            // You can relax this if overpayment is allowed
            throw new BusinessException("Payment exceeds total fee amount");
        }

        PaymentStatus status;
        if (paidAfter.compareTo(BigDecimal.ZERO) == 0) {
            status = PaymentStatus.PENDING;
        } else if (paidAfter.compareTo(totalFee) < 0) {
            status = PaymentStatus.PARTIALLY_PAID;
        } else {
            status = PaymentStatus.PAID;
        }

        Payment payment = Payment.builder()
                .student(student)
                .course(course)
                .batch(batch)
                .amount(amount)
                .totalFee(totalFee)
                .discountAmount(request.getDiscountAmount())
                .method(request.getMethod())
                .status(status)
                .referenceNumber(request.getReferenceNumber())
                .remarks(request.getRemarks())
                .paymentDate(request.getPaymentDate() != null
                        ? request.getPaymentDate()
                        : LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);

        return toResponse(payment, paidAfter, due.max(BigDecimal.ZERO));
    }

    @Transactional(readOnly = true)
    public PaymentResponse get(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        BigDecimal totalPaid =
                paymentRepository.getTotalPaidForStudentCourse(
                        payment.getStudent().getId(),
                        payment.getCourse().getId());

        BigDecimal due = payment.getTotalFee().subtract(totalPaid);
        if (due.compareTo(BigDecimal.ZERO) < 0) {
            due = BigDecimal.ZERO;
        }

        return toResponse(payment, totalPaid, due);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll().stream()
                .map(p -> {
                    BigDecimal totalPaid =
                            paymentRepository.getTotalPaidForStudentCourse(
                                    p.getStudent().getId(), p.getCourse().getId());
                    BigDecimal due = p.getTotalFee().subtract(totalPaid);
                    if (due.compareTo(BigDecimal.ZERO) < 0) {
                        due = BigDecimal.ZERO;
                    }
                    return toResponse(p, totalPaid, due);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getByStudent(Long studentId) {
        return paymentRepository.findByStudentId(studentId).stream()
                .map(p -> {
                    BigDecimal totalPaid =
                            paymentRepository.getTotalPaidForStudentCourse(
                                    p.getStudent().getId(), p.getCourse().getId());
                    BigDecimal due = p.getTotalFee().subtract(totalPaid);
                    if (due.compareTo(BigDecimal.ZERO) < 0) {
                        due = BigDecimal.ZERO;
                    }
                    return toResponse(p, totalPaid, due);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Object[]> getMonthlyRevenue() {
        return paymentRepository.getMonthlyRevenue();
    }


    @Transactional(readOnly = true)
    public DashboardFinanceSummary getDashboardFinanceSummary() {
        // 1) Revenue all time
        BigDecimal totalRevenueAllTime = paymentRepository.getTotalRevenueAllTime();

        // 2) Revenue this month
        LocalDate today = LocalDate.now();
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfNextMonth = startOfMonth.plusMonths(1);

        BigDecimal totalRevenueThisMonth =
                paymentRepository.getTotalRevenueBetween(startOfMonth, startOfNextMonth);

        // 3) Outstanding due = sum over each (student, course) of max(totalFee - paid, 0)
        Map<String, StudentCourseTotals> map = new HashMap<>();
        for (Payment p : paymentRepository.findAll()) {
            String key = p.getStudent().getId() + ":" + p.getCourse().getId();
            StudentCourseTotals totals = map.computeIfAbsent(
                    key,
                    k -> new StudentCourseTotals(
                            p.getTotalFee() != null ? p.getTotalFee() : BigDecimal.ZERO,
                            BigDecimal.ZERO
                    )
            );

            if (p.getTotalFee() != null) {
                // Take the latest known total fee
                totals.totalFee = p.getTotalFee();
            }

            totals.amountPaid = totals.amountPaid.add(p.getAmount());
        }

        BigDecimal outstandingDue = BigDecimal.ZERO;
        for (StudentCourseTotals totals : map.values()) {
            BigDecimal due = totals.totalFee.subtract(totals.amountPaid);
            if (due.compareTo(BigDecimal.ZERO) > 0) {
                outstandingDue = outstandingDue.add(due);
            }
        }

        return DashboardFinanceSummary.builder()
                .totalRevenueAllTime(totalRevenueAllTime)
                .totalRevenueThisMonth(totalRevenueThisMonth)
                .totalOutstandingDue(outstandingDue)
                .build();
    }

    // helper class inside PaymentService
    private static class StudentCourseTotals {
        BigDecimal totalFee;
        BigDecimal amountPaid;

        StudentCourseTotals(BigDecimal totalFee, BigDecimal amountPaid) {
            this.totalFee = totalFee != null ? totalFee : BigDecimal.ZERO;
            this.amountPaid = amountPaid != null ? amountPaid : BigDecimal.ZERO;
        }
    }


    private PaymentResponse toResponse(Payment p, BigDecimal totalPaid, BigDecimal due) {
        return PaymentResponse.builder()
                .id(p.getId())
                .studentId(p.getStudent().getId())
                .studentName(p.getStudent().getFullName())
                .courseId(p.getCourse().getId())
                .courseName(p.getCourse().getName())
                .batchId(p.getBatch() != null ? p.getBatch().getId() : null)
                .batchName(p.getBatch() != null ? p.getBatch().getName() : null)
                .amount(p.getAmount())
                .totalFee(p.getTotalFee())
                .discountAmount(p.getDiscountAmount())
                .totalPaidSoFar(totalPaid)
                .dueAmount(due)
                .method(p.getMethod())
                .status(p.getStatus())
                .referenceNumber(p.getReferenceNumber())
                .paymentDate(p.getPaymentDate())
                .remarks(p.getRemarks())
                .build();
    }
}
