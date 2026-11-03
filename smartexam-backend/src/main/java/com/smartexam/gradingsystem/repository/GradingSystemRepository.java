package com.smartexam.gradingsystem.repository;

import com.smartexam.gradingsystem.entity.GradingSystem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradingSystemRepository
        extends JpaRepository<GradingSystem, Long> {

    List<GradingSystem> findByOrganization_Id(Long organizationId);

    List<GradingSystem> findByOrganization_IdAndActiveTrue(Long organizationId);

    Optional<GradingSystem> findByIdAndOrganization_Id(
            Long id,
            Long organizationId
    );

    boolean existsByOrganization_IdAndCode(
            Long organizationId,
            String code
    );

    boolean existsByOrganization_IdAndCodeAndIdNot(
            Long organizationId,
            String code,
            Long id
    );
}