package com.smartexam.institution.settings.service.impl;

import com.smartexam.institution.settings.dto.CreateInstitutionSettingsRequest;
import com.smartexam.institution.settings.dto.InstitutionSettingsResponse;
import com.smartexam.institution.settings.dto.UpdateInstitutionSettingsRequest;
import com.smartexam.institution.settings.entity.InstitutionSettings;
import com.smartexam.institution.settings.repository.InstitutionSettingsRepository;

import com.smartexam.institution.settings.service.InstitutionSettingsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class InstitutionSettingsServiceImpl
        implements InstitutionSettingsService {

    private final InstitutionSettingsRepository repository;

    @Override
    public InstitutionSettingsResponse create(
            CreateInstitutionSettingsRequest request
    ) {

        if (repository.existsByOrganizationId(
                request.organizationId()
        )) {
            throw new IllegalArgumentException(
                    "Institution settings already exist for organization: "
                            + request.organizationId()
            );
        }

        if (repository.existsByInstitutionCode(
                request.institutionCode()
        )) {
            throw new IllegalArgumentException(
                    "Institution code already exists: "
                            + request.institutionCode()
            );
        }

        InstitutionSettings settings = InstitutionSettings.builder()
                .organizationId(request.organizationId())
                .institutionName(request.institutionName())
                .institutionCode(request.institutionCode())
                .email(request.email())
                .phone(request.phone())
                .website(request.website())
                .address(request.address())
                .city(request.city())
                .state(request.state())
                .country(request.country())
                .postalCode(request.postalCode())
                .logoUrl(request.logoUrl())
                .active(
                        request.active() != null
                                ? request.active()
                                : Boolean.TRUE
                )
                .createdAt(LocalDate.now().atStartOfDay())
                .updatedAt(LocalDate.now().atStartOfDay())
                .build();

        InstitutionSettings saved = repository.save(settings);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InstitutionSettingsResponse getByOrganizationId(
            Long organizationId
    ) {

        InstitutionSettings settings = repository
                .findByOrganizationId(organizationId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Institution settings not found for organization: "
                                        + organizationId
                        )
                );

        return mapToResponse(settings);
    }

    @Override
    public InstitutionSettingsResponse update(
            Long organizationId,
            UpdateInstitutionSettingsRequest request
    ) {

        InstitutionSettings settings = repository
                .findByOrganizationId(organizationId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Institution settings not found for organization: "
                                        + organizationId
                        )
                );

        if (!settings.getInstitutionCode()
                .equalsIgnoreCase(request.institutionCode())
                && repository.existsByInstitutionCodeAndIdNot(
                request.institutionCode(),
                settings.getId()
        )) {
            throw new IllegalArgumentException(
                    "Institution code already exists: "
                            + request.institutionCode()
            );
        }

        settings.setInstitutionName(
                request.institutionName()
        );

        settings.setInstitutionCode(
                request.institutionCode()
        );

        settings.setEmail(
                request.email()
        );

        settings.setPhone(
                request.phone()
        );

        settings.setWebsite(
                request.website()
        );

        settings.setAddress(
                request.address()
        );

        settings.setCity(
                request.city()
        );

        settings.setState(
                request.state()
        );

        settings.setCountry(
                request.country()
        );

        settings.setPostalCode(
                request.postalCode()
        );

        settings.setLogoUrl(
                request.logoUrl()
        );

        settings.setActive(
                request.active()
        );

        InstitutionSettings updated = repository.save(settings);

        return mapToResponse(updated);
    }

    private InstitutionSettingsResponse mapToResponse(
            InstitutionSettings settings
    ) {

        return new InstitutionSettingsResponse(
                settings.getId(),
                settings.getOrganizationId(),
                settings.getInstitutionName(),
                settings.getInstitutionCode(),
                settings.getEmail(),
                settings.getPhone(),
                settings.getWebsite(),
                settings.getAddress(),
                settings.getCity(),
                settings.getState(),
                settings.getCountry(),
                settings.getPostalCode(),
                settings.getLogoUrl(),
                settings.isActive(),
                settings.getCreatedAt(),
                settings.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public void delete(Long organizationId) {

        InstitutionSettings settings =
                repository
                        .findByOrganizationId(organizationId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Institution settings not found for organization: "
                                                + organizationId
                                )
                        );

        settings.setActive(false);

        repository.save(settings);
    }

    @Override
    @Transactional
    public void activate(Long organizationId) {

        InstitutionSettings settings =
                repository
                        .findByOrganizationId(organizationId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Institution settings not found for organization: "
                                                + organizationId
                                )
                        );

        if (settings.isActive()) {
            throw new IllegalStateException(
                    "Institute is already active"
            );
        }

        settings.setActive(true);

        repository.save(settings);
    }

    @Override
    @Transactional
    public void deactivate(Long organizationId) {

        InstitutionSettings settings =
                repository
                        .findByOrganizationId(organizationId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Institution settings not found for organization: "
                                                + organizationId
                                )
                        );

        if (!settings.isActive()) {
            throw new IllegalStateException(
                    "Institute is already deactivated"
            );
        }

        settings.setActive(false);

        repository.save(settings);
    }
}