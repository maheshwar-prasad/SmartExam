package com.smartexam.examresult.repository;

import com.smartexam.examresult.entity.ExamResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExamResultRepository
        extends JpaRepository<ExamResult, Long>,
        JpaSpecificationExecutor<ExamResult> {

    /**
     * Check whether a result already exists
     * for a student and exam.
     */
    boolean existsByStudentIdAndExamId(
            Long studentId,
            Long examId
    );

    /**
     * Find result for a specific student and exam.
     */
    Optional<ExamResult> findByStudentIdAndExamId(
            Long studentId,
            Long examId
    );

    /**
     * Get all active results for a student.
     */
    List<ExamResult> findByStudentIdAndActiveTrue(
            Long studentId
    );

    /**
     * Get all active results for an exam.
     */
    List<ExamResult> findByExamIdAndActiveTrue(
            Long examId
    );

    /**
     * Get result by organization.
     */
    List<ExamResult> findByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    /**
     * Paginated active results.
     */
    Page<ExamResult> findByActiveTrue(
            Pageable pageable
    );

    /**
     * Optional: Get published results
     * for a specific student.
     */
    List<ExamResult> findByStudentIdAndPublishedTrueAndActiveTrue(
            Long studentId
    );
    long countByActiveTrue();

    long countByActiveTrueAndResultStatus(String resultStatus);

    @Query("""
        SELECT COALESCE(AVG(r.percentage), 0)
        FROM ExamResult r
        WHERE r.active = true
        """)
    BigDecimal findAveragePercentage();

    @Query("""
        SELECT COALESCE(MAX(r.percentage), 0)
        FROM ExamResult r
        WHERE r.active = true
        """)
    BigDecimal findHighestPercentage();

    @Query("""
        SELECT COALESCE(MIN(r.percentage), 0)
        FROM ExamResult r
        WHERE r.active = true
        """)
    BigDecimal findLowestPercentage();

}