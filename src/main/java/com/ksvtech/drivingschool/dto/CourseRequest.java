package com.ksvtech.drivingschool.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CourseRequest {

    @NotBlank
    private String code;        // e.g. "LMV-21D"

    @NotBlank
    private String name;        // e.g. "LMV 21-Day Course"

    @Min(value = 1, message = "Duration must be at least 1 day")
    private int durationDays;

    @Positive(message = "Fees must be positive")
    private double fees;
}
