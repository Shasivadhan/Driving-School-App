package com.ksvtech.drivingschool.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(length = 20)
    private String mobile;

    @Column(length = 120)
    private String email;

    @Column(length = 50)
    private String licenseNumber;

    @Column(length = 50)
    private String licenseType; // e.g. LMV, MCWG, HMV

    private Integer experienceYears;

    private LocalDate joinedDate;

    @Column(nullable = false)
    private boolean active = true;

    @Column(length = 500)
    private String notes;
}
