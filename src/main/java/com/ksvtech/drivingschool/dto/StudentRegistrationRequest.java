package com.ksvtech.drivingschool.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRegistrationRequest {

    @NotBlank
    private String fullName;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number")
    private String mobile;

    @NotBlank
    @Size(min = 12, max = 12, message = "Aadhaar must be 12 digits")
    private String aadharNumber;

    @Email
    private String email;

    private String address;

    @Past
    private LocalDate dob;
}
