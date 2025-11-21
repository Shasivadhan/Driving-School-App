package com.ksvtech.drivingschool.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseResponse {
    private Long id;
    private String code;
    private String name;
    private int durationDays;
    private double fees;
}
