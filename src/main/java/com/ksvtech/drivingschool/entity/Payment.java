package com.ksvtech.drivingschool.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch; // optional

    /** This payment transaction amount */
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    /** Agreed total course fee for this student+course */
    @Column(name = "total_fee", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalFee;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PaymentStatus status;

    /** UPI ref / cheque no / transaction ID etc */
    @Column(length = 50)
    private String referenceNumber;

    private LocalDateTime paymentDate;

    @Column(length = 255)
    private String remarks;
}
