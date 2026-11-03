package com.smartexam.alumni.repository;

import com.smartexam.alumni.entity.Alumni;
import com.smartexam.alumni.enums.AlumniStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlumniRepository
        extends JpaRepository<Alumni, Long> {

    Optional<Alumni> findByIdAndActiveTrue(Long id);

    Optional<Alumni> findByIdAndOrganizationId(
            Long id,
            Long organizationId
    );

    Optional<Alumni> findByStudentIdAndOrganizationId(
            Long studentId,
            Long organizationId
    );

    boolean existsByStudentIdAndOrganizationId(
            Long studentId,
            Long organizationId
    );

    Page<Alumni> findByOrganizationIdAndActiveTrue(
            Long organizationId,
            Pageable pageable
    );

    Page<Alumni> findByOrganizationIdAndStatus(
            Long organizationId,
            AlumniStatus status,
            Pageable pageable
    );
}