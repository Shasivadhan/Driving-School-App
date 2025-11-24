package com.ksvtech.drivingschool.repository;

import com.ksvtech.drivingschool.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import org.springframework.data.repository.query.Param;



import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByStudentId(Long studentId);

    List<Payment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("select coalesce(sum(p.amount), 0) " +
            "from Payment p " +
            "where p.student.id = :studentId and p.course.id = :courseId")
    BigDecimal getTotalPaidForStudentCourse(@Param("studentId") Long studentId,
                                            @Param("courseId") Long courseId);
    @Query("select coalesce(sum(p.amount), 0) from Payment p")
    BigDecimal getTotalRevenueAllTime();

    @Query("select coalesce(sum(p.amount), 0) " +
            "from Payment p " +
            "where p.paymentDate >= :from and p.paymentDate < :to")
    BigDecimal getTotalRevenueBetween(@Param("from") LocalDateTime from,
                                      @Param("to") LocalDateTime to);

    @Query("select function('DATE_FORMAT', p.paymentDate, '%Y-%m') as ym, " +
            "       coalesce(sum(p.amount), 0) " +
            "from Payment p " +
            "where p.paymentDate is not null " +
            "group by function('DATE_FORMAT', p.paymentDate, '%Y-%m') " +
            "order by ym")
    List<Object[]> getMonthlyRevenue();

}
