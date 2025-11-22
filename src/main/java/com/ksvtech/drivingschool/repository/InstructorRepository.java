package com.ksvtech.drivingschool.repository;

import com.ksvtech.drivingschool.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
