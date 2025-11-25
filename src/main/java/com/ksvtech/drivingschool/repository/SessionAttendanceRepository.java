package com.ksvtech.drivingschool.repository;

import com.ksvtech.drivingschool.entity.SessionAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionAttendanceRepository extends JpaRepository<SessionAttendance, Long> {

    List<SessionAttendance> findBySessionId(Long sessionId);

    boolean existsBySessionIdAndStudentId(Long sessionId, Long studentId);
}
