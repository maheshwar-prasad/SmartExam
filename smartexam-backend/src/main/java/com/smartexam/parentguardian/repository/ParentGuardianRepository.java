package com.smartexam.parentguardian.repository;

import com.smartexam.parentguardian.entity.ParentGuardian;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ParentGuardianRepository
        extends JpaRepository<ParentGuardian, Long>,
        JpaSpecificationExecutor<ParentGuardian> {

    Optional<ParentGuardian> findByIdAndActiveTrue(Long id);

    Page<ParentGuardian> findByOrganizationIdAndActiveTrue(
            Long organizationId,
            Pageable pageable
    );

    boolean existsByOrganizationIdAndMobile(
            Long organizationId,
            String mobile
    );

    boolean existsByOrganizationIdAndEmail(
            Long organizationId,
            String email
    );

    Optional<ParentGuardian> findByOrganizationIdAndMobile(
            Long organizationId,
            String mobile
    );

    Optional<ParentGuardian> findByOrganizationIdAndEmail(
            Long organizationId,
            String email
    );
}