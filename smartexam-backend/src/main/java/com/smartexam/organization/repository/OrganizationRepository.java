package com.smartexam.organization.repository;

import com.smartexam.organization.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OrganizationRepository
        extends JpaRepository<Organization, Long>,
        JpaSpecificationExecutor<Organization> {

    Optional<Organization> findByCodeIgnoreCase(String code);

    Optional<Organization> findByEmailIgnoreCase(String email);

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByCodeIgnoreCaseAndIdNot(String code, Long id);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    Page<Organization> findByActiveTrue(Pageable pageable);

    long countByActiveTrue();

    long countById(Long id);

    long countByIdAndActiveTrue(Long id);
}