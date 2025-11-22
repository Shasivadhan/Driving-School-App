package com.ksvtech.drivingschool.repository;

import com.ksvtech.drivingschool.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    Optional<Certificate> findByCertificateNumber(String certificateNumber);
    // NEW: certificates grouped by course name
    @Query("SELECT c.course.name, COUNT(c) " +
            "FROM Certificate c " +
            "GROUP BY c.course.name " +
            "ORDER BY c.course.name")
    List<Object[]> countCertificatesByCourse();

    // NEW: certificates grouped by month (YYYY-MM) using MySQL DATE_FORMAT
    @Query("SELECT FUNCTION('DATE_FORMAT', c.issueDate, '%Y-%m') AS month, COUNT(c) " +
            "FROM Certificate c " +
            "GROUP BY month " +
            "ORDER BY month")
    List<Object[]> countCertificatesPerMonth();
}
