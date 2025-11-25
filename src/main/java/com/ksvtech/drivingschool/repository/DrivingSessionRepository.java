package com.ksvtech.drivingschool.repository;

import com.ksvtech.drivingschool.entity.DrivingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DrivingSessionRepository extends JpaRepository<DrivingSession, Long> {

    List<DrivingSession> findByBatchIdOrderBySessionDateDescStartTimeDesc(Long batchId);

    List<DrivingSession> findBySessionDateOrderByStartTimeAsc(LocalDate date);
}
