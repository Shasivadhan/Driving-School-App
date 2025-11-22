package com.ksvtech.drivingschool.dto;

import com.ksvtech.drivingschool.entity.BatchStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class BatchResponse {

    private Long id;
    private String name;

    private Long courseId;
    private String courseName;

    private Long instructorId;
    private String instructorName;

    private Long vehicleId;
    private String vehicleRegistration;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalTime startTime;
    private LocalTime endTime;

    private Integer capacity;
    private int enrolledCount;

    private BatchStatus status;

    private List<Long> studentIds;
}
