package com.smartexam.examregistration.repository;

import com.smartexam.examregistration.entity.StudentExamRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface StudentExamRegistrationRepository
        extends JpaRepository<StudentExamRegistration, Long>,
        JpaSpecificationExecutor<StudentExamRegistration> {

    /**
     * Get all active registrations.
     */
    List<StudentExamRegistration> findByActiveTrue();

    /**
     * Get paginated active registrations.
     */
    Page<StudentExamRegistration> findByActiveTrue(
            Pageable pageable
    );

    /**
     * Get registrations by student.
     */
    List<StudentExamRegistration> findByStudentIdAndActiveTrue(
            Long studentId
    );

    /**
     * Get registrations by exam.
     */
    List<StudentExamRegistration> findByExamIdAndActiveTrue(
            Long examId
    );

    /**
     * Get registrations by organization.
     */
    List<StudentExamRegistration>
    findByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    /**
     * Check whether the student is already
     * registered for the exam.
     */
    boolean existsByStudentIdAndExamId(
            Long studentId,
            Long examId
    );

    /**
     * Duplicate validation during update.
     */
    boolean existsByStudentIdAndExamIdAndIdNot(
            Long studentId,
            Long examId,
            Long id
    );

    /**
     * Find registration by registration number.
     */
    Optional<StudentExamRegistration>
    findByRegistrationNumber(
            String registrationNumber
    );

    /**
     * Check duplicate registration number.
     */
    boolean existsByRegistrationNumber(
            String registrationNumber
    );

    /**
     * Check duplicate registration number
     * excluding the current record.
     */
    boolean existsByRegistrationNumberAndIdNot(
            String registrationNumber,
            Long id
    );

    /**
     * Used later by Student Marks module
     * to validate whether a student is
     * registered for an exam.
     */
    boolean existsByStudentIdAndExamIdAndStatusAndActiveTrue(
            Long studentId,
            Long examId,
            String status
    );

}