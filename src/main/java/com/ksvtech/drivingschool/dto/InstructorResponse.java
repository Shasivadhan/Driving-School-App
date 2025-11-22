package com.ksvtech.drivingschool.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class InstructorResponse {

    private Long id;
    private String fullName;
    private String mobile;
    private String email;
    private String licenseNumber;
    private String licenseType;
    private Integer experienceYears;
    private LocalDate joinedDate;
    private boolean active;
}
