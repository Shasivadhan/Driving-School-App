package com.ksvtech.drivingschool.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InstructorRequest {

    @NotBlank
    private String fullName;

    private String mobile;
    private String email;
    private String licenseNumber;
    private String licenseType;
    private Integer experienceYears;
    private LocalDate joinedDate;
    private Boolean active;
    private String notes;
}
