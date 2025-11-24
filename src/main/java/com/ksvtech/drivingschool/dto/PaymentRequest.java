package com.ksvtech.drivingschool.dto;

import com.ksvtech.drivingschool.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

    /** Optional: link payment to a batch */
    private Long batchId;

    /** This payment amount */
    @NotNull
    @Positive
    private BigDecimal amount;

    /** Agreed total course fee */
    @NotNull
    @Positive
    private BigDecimal totalFee;

    @PositiveOrZero
    private BigDecimal discountAmount;

    @NotNull
    private PaymentMethod method;

    private String referenceNumber;

    private String remarks;

    /** Optional custom payment date; if null, set to now */
    private LocalDateTime paymentDate;
}
