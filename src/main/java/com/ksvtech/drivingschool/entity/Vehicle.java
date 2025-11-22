package com.ksvtech.drivingschool.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String registrationNumber; // KA-01-AB-1234

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private VehicleType type; // CAR/BIKE/HMV

    @Column(length = 50)
    private String make; // e.g. Maruti, Honda

    @Column(length = 50)
    private String model; // e.g. Swift, Activa

    private Integer yearOfManufacture;

    @Column(length = 20)
    private String fuelType; // Petrol/Diesel/EV

    private LocalDate insuranceExpiryDate;

    private LocalDate fitnessExpiryDate;

    @Column(nullable = false)
    private boolean active = true;

    @Column(length = 500)
    private String notes;
}
