package com.ksvtech.drivingschool.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SessionRequest {

    private Long batchId;
    private Long instructorId;
    private Long vehicleId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sessionDate;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private String location;
    private String notes;
}
