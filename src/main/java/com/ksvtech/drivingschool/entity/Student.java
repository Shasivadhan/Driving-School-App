package com.ksvtech.drivingschool.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private String mobile;

    @Column(unique = true, length = 12)
    private String aadharLast4;

    @Column(nullable = false)
    private String aadharHash;

    private String email;

    private String address;

    private LocalDate dob;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
