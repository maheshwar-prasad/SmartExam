package com.smartexam.institution.academicyear.repository;

import com.smartexam.institution.academicyear.entity.AcademicYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcademicYearRepository
        extends JpaRepository<AcademicYear, Long> {

    Page<AcademicYear> findByOrganizationId(
            Long organizationId,
            Pageable pageable
    );

    Optional<AcademicYear> findByIdAndOrganizationId(
            Long id,
            Long organizationId
    );

    boolean existsByOrganizationIdAndCode(
            Long organizationId,
            String code
    );

    boolean existsByOrganizationIdAndCodeAndIdNot(
            Long organizationId,
            String code,
            Long id
    );
}