package com.ksvtech.drivingschool.dto;

import com.ksvtech.drivingschool.entity.PaymentMethod;
import com.ksvtech.drivingschool.entity.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseName;

    private Long batchId;
    private String batchName;

    private BigDecimal amount;          // this payment
    private BigDecimal totalFee;        // agreed total fee
    private BigDecimal discountAmount;

    private BigDecimal totalPaidSoFar;  // all payments sum
    private BigDecimal dueAmount;       // totalFee - totalPaidSoFar

    private PaymentMethod method;
    private PaymentStatus status;

    private String referenceNumber;
    private LocalDateTime paymentDate;
    private String remarks;
}
