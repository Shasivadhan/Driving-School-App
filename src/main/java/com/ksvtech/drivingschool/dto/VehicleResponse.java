package com.ksvtech.drivingschool.dto;

import com.ksvtech.drivingschool.entity.VehicleType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class VehicleResponse {

    private Long id;
    private String registrationNumber;
    private VehicleType type;
    private String make;
    private String model;
    private Integer yearOfManufacture;
    private String fuelType;
    private LocalDate insuranceExpiryDate;
    private LocalDate fitnessExpiryDate;
    private boolean active;
}
