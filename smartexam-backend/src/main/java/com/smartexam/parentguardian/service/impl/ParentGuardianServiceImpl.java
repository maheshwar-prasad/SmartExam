package com.smartexam.parentguardian.service.impl;

import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.parentguardian.dto.ParentGuardianRequest;
import com.smartexam.parentguardian.dto.ParentGuardianResponse;
import com.smartexam.parentguardian.entity.ParentGuardian;
import com.smartexam.parentguardian.repository.ParentGuardianRepository;
import com.smartexam.parentguardian.service.ParentGuardianService;
import com.smartexam.security.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ParentGuardianServiceImpl
        implements ParentGuardianService {

    private final ParentGuardianRepository parentGuardianRepository;

    private final CurrentUserService currentUserService;
    // =========================================================
    // Create
    // =========================================================

    @Override
    public ParentGuardianResponse create(
            ParentGuardianRequest request) {

        validateDuplicateContact(request, null);

        /*
         * TODO:
         * Replace this with your actual CurrentUserService /
         * organization context once we wire it into this module.
         *
         * For now organizationId is obtained from the
         * authenticated user's organization.
         */
        Long organizationId = getCurrentOrganizationId();

        ParentGuardian parentGuardian =
                ParentGuardian.builder()
                        .organizationId(organizationId)
                        .firstName(request.getFirstName().trim())
                        .lastName(normalize(request.getLastName()))
                        .mobile(normalize(request.getMobile()))
                        .alternateMobile(
                                normalize(request.getAlternateMobile()))
                        .email(normalize(request.getEmail()))
                        .occupation(
                                normalize(request.getOccupation()))
                        .address(normalize(request.getAddress()))
                        .city(normalize(request.getCity()))
                        .state(normalize(request.getState()))
                        .pincode(normalize(request.getPincode()))
                        .active(true)
                        .build();

        ParentGuardian saved =
                parentGuardianRepository.save(parentGuardian);

        log.info(
                "Parent/Guardian created successfully. id={}, organizationId={}",
                saved.getId(),
                organizationId
        );

        return toResponse(saved);
    }

    // =========================================================
    // Update
    // =========================================================

    @Override
    public ParentGuardianResponse update(
            Long id,
            ParentGuardianRequest request) {

        ParentGuardian parentGuardian =
                getEntity(id);

        validateDuplicateContact(request, id);

        parentGuardian.setFirstName(
                request.getFirstName().trim()
        );

        parentGuardian.setLastName(
                normalize(request.getLastName())
        );

        parentGuardian.setMobile(
                normalize(request.getMobile())
        );

        parentGuardian.setAlternateMobile(
                normalize(request.getAlternateMobile())
        );

        parentGuardian.setEmail(
                normalize(request.getEmail())
        );

        parentGuardian.setOccupation(
                normalize(request.getOccupation())
        );

        parentGuardian.setAddress(
                normalize(request.getAddress())
        );

        parentGuardian.setCity(
                normalize(request.getCity())
        );

        parentGuardian.setState(
                normalize(request.getState())
        );

        parentGuardian.setPincode(
                normalize(request.getPincode())
        );

        ParentGuardian saved =
                parentGuardianRepository.save(parentGuardian);

        log.info(
                "Parent/Guardian updated successfully. id={}",
                saved.getId()
        );

        return toResponse(saved);
    }

    // =========================================================
    // Get By ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public ParentGuardianResponse getById(Long id) {

        return toResponse(getEntity(id));
    }

    // =========================================================
    // Get All
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<ParentGuardianResponse> getAll(
            Pageable pageable) {

        Long organizationId =
                getCurrentOrganizationId();

        return parentGuardianRepository
                .findByOrganizationIdAndActiveTrue(
                        organizationId,
                        pageable
                )
                .map(this::toResponse);
    }

    // =========================================================
    // Deactivate
    // =========================================================

    @Override
    public void deactivate(Long id) {

        ParentGuardian parentGuardian =
                getEntity(id);

        if (!Boolean.TRUE.equals(parentGuardian.getActive())) {
            return;
        }

        parentGuardian.setActive(false);

        parentGuardianRepository.save(parentGuardian);

        log.info(
                "Parent/Guardian deactivated. id={}",
                id
        );
    }

    // =========================================================
    // Activate
    // =========================================================

    @Override
    public void activate(Long id) {

        ParentGuardian parentGuardian =
                getEntityForStatusChange(id);

        if (Boolean.TRUE.equals(parentGuardian.getActive())) {
            return;
        }

        parentGuardian.setActive(true);

        parentGuardianRepository.save(parentGuardian);

        log.info(
                "Parent/Guardian activated. id={}",
                id
        );
    }

    // =========================================================
    // Find Entity
    // =========================================================

    private ParentGuardian getEntity(Long id) {

        Long organizationId =
                getCurrentOrganizationId();

        return parentGuardianRepository
                .findByIdAndActiveTrue(id)
                .filter(parent ->
                        organizationId.equals(
                                parent.getOrganizationId()
                        )
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parent/Guardian not found with id : "
                                        + id
                        )
                );
    }

    private ParentGuardian getEntityForStatusChange(Long id) {

        Long organizationId =
                getCurrentOrganizationId();

        return parentGuardianRepository
                .findById(id)
                .filter(parent ->
                        organizationId.equals(
                                parent.getOrganizationId()
                        )
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parent/Guardian not found with id : "
                                        + id
                        )
                );
    }

    // =========================================================
    // Duplicate Validation
    // =========================================================

    private void validateDuplicateContact(
            ParentGuardianRequest request,
            Long excludeId) {

        Long organizationId =
                getCurrentOrganizationId();

        String mobile =
                normalize(request.getMobile());

        String email =
                normalize(request.getEmail());

        if (mobile != null) {

            parentGuardianRepository
                    .findByOrganizationIdAndMobile(
                            organizationId,
                            mobile
                    )
                    .ifPresent(existing -> {

                        if (!existing.getId().equals(excludeId)) {

                            throw new DuplicateResourceException(
                                    "Parent/Guardian mobile",
                                    mobile
                            );
                        }
                    });
        }

        if (email != null) {

            parentGuardianRepository
                    .findByOrganizationIdAndEmail(
                            organizationId,
                            email
                    )
                    .ifPresent(existing -> {

                        if (!existing.getId().equals(excludeId)) {

                            throw new DuplicateResourceException(
                                    "Parent/Guardian email",
                                    email
                            );
                        }
                    });
        }
    }

    // =========================================================
    // Response Mapping
    // =========================================================

    private ParentGuardianResponse toResponse(
            ParentGuardian entity) {

        String fullName =
                buildFullName(
                        entity.getFirstName(),
                        entity.getLastName()
                );

        return ParentGuardianResponse.builder()
                .id(entity.getId())
                .organizationId(entity.getOrganizationId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .fullName(fullName)
                .mobile(entity.getMobile())
                .alternateMobile(entity.getAlternateMobile())
                .email(entity.getEmail())
                .occupation(entity.getOccupation())
                .address(entity.getAddress())
                .city(entity.getCity())
                .state(entity.getState())
                .pincode(entity.getPincode())
                .active(entity.getActive())
                .createdAt(
                        entity.getCreatedAt() != null
                                ? entity.getCreatedAt().toString()
                                : null
                )
                .updatedAt(
                        entity.getUpdatedAt() != null
                                ? entity.getUpdatedAt().toString()
                                : null
                )
                .build();
    }

    // =========================================================
    // Helpers
    // =========================================================

    private String normalize(String value) {

        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isEmpty()
                ? null
                : trimmed;
    }

    private String buildFullName(
            String firstName,
            String lastName) {

        String first =
                firstName == null
                        ? ""
                        : firstName.trim();

        String last =
                lastName == null
                        ? ""
                        : lastName.trim();

        return (first + " " + last)
                .trim();
    }

    private Long getCurrentOrganizationId() {
        return currentUserService.getOrganizationId();
    }
}