package com.smartexam.institution.settings.repository;

import com.smartexam.institution.settings.entity.InstitutionSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstitutionSettingsRepository
        extends JpaRepository<InstitutionSettings, Long> {

    Optional<InstitutionSettings> findByOrganizationId(
            Long organizationId
    );

    boolean existsByOrganizationId(
            Long organizationId
    );

    boolean existsByInstitutionCode(
            String institutionCode
    );

    boolean existsByInstitutionCodeAndIdNot(
            String institutionCode,
            Long id
    );
}