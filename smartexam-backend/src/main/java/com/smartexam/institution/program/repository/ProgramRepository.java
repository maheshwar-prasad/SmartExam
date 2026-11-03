package com.smartexam.institution.program.repository;

import com.smartexam.institution.program.entity.Program;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository
        extends JpaRepository<Program, Long> {

    List<Program> findByOrganizationIdOrderByNameAsc(
            Long organizationId
    );

    List<Program> findByOrganizationIdAndActiveOrderByNameAsc(
            Long organizationId,
            Boolean active
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

    Optional<Program> findByOrganizationIdAndId(
            Long organizationId,
            Long id
    );

    Page<Program> findByOrganizationId(
            Long organizationId,
            Pageable pageable
    );
}