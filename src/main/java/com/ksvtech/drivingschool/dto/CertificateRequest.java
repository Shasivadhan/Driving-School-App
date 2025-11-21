package com.ksvtech.drivingschool.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CertificateRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

    @NotNull
    @Size(min = 12, max = 12)
    private String aadharNumber;
}
