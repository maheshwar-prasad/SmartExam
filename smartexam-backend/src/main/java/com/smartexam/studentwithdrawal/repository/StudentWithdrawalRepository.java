package com.smartexam.studentwithdrawal.repository;

import com.smartexam.studentwithdrawal.entity.StudentWithdrawal;
import com.smartexam.studentwithdrawal.enums.WithdrawalStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentWithdrawalRepository
        extends JpaRepository<StudentWithdrawal, Long> {

    Optional<StudentWithdrawal>
    findByIdAndActiveTrue(Long id);

    Optional<StudentWithdrawal>
    findByIdAndOrganizationId(
            Long id,
            Long organizationId
    );

    Optional<StudentWithdrawal>
    findByStudentIdAndOrganizationId(
            Long studentId,
            Long organizationId
    );

    boolean existsByStudentIdAndOrganizationId(
            Long studentId,
            Long organizationId
    );

    Page<StudentWithdrawal>
    findByOrganizationIdAndActiveTrue(
            Long organizationId,
            Pageable pageable
    );

    Page<StudentWithdrawal>
    findByOrganizationIdAndStatus(
            Long organizationId,
            WithdrawalStatus status,
            Pageable pageable
    );
}