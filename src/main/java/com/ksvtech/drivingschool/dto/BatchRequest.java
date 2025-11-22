package com.ksvtech.drivingschool.dto;

import com.ksvtech.drivingschool.entity.BatchStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BatchRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long courseId;

    @NotNull
    private Long instructorId;

    @NotNull
    private Long vehicleId;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalTime startTime;
    private LocalTime endTime;

    private Integer capacity;

    private BatchStatus status; // optional, default PLANNED

    // optional: initial student IDs to enroll
    private List<Long> studentIds;
}
