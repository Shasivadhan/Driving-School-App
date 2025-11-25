package com.ksvtech.drivingschool.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "session_attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id")
    private DrivingSession session;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AttendanceStatus status;

    @Column(length = 500)
    private String remarks;
}
