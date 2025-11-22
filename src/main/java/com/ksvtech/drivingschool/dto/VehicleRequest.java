package com.ksvtech.drivingschool.dto;

import com.ksvtech.drivingschool.entity.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VehicleRequest {

    @NotBlank
    private String registrationNumber;

    @NotNull
    private VehicleType type;

    private String make;
    private String model;
    private Integer yearOfManufacture;
    private String fuelType;
    private LocalDate insuranceExpiryDate;
    private LocalDate fitnessExpiryDate;
    private Boolean active;
    private String notes;
}
