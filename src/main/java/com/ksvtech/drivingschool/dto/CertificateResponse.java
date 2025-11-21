package com.ksvtech.drivingschool.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CertificateResponse {
    private Long id;
    private String certificateNumber;
    private Long studentId;
    private String studentName;
    private String courseName;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String qrData;
}
