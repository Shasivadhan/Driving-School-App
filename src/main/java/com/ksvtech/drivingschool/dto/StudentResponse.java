package com.ksvtech.drivingschool.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StudentResponse {
    private Long id;
    private String fullName;
    private String mobile;
    private String email;
    private String address;
    private LocalDate dob;
    // ❌ REMOVE this line if it still exists:
    // private String username;
}
