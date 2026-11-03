package com.smartexam.organization.service.impl;

import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.organization.dto.OrganizationRequest;
import com.smartexam.organization.dto.OrganizationResponse;
import com.smartexam.organization.dto.OrganizationUpdateRequest;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.mapper.OrganizationMapper;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationServiceImpl
        implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    @Override
    public OrganizationResponse create(
            OrganizationRequest request) {

        validateDuplicateForCreate(request);

        Organization organization =
                organizationMapper.toEntity(request);

        organization.setActive(true);

        Organization saved =
                organizationRepository.save(organization);

        return organizationMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationResponse getById(Long id) {

        Organization organization =
                findOrganization(id);

        return organizationMapper.toResponse(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationResponse> getAll() {

        return organizationMapper.toResponseList(
                organizationRepository.findByActiveTrue(
                        PageRequest.of(
                                0,
                                Integer.MAX_VALUE
                        )
                ).getContent()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortDirection, sortBy)
                );

        return organizationRepository
                .findByActiveTrue(pageable)
                .map(organizationMapper::toResponse);
    }

    @Override
    public OrganizationResponse update(
            Long id,
            OrganizationUpdateRequest request) {

        Organization organization =
                findOrganization(id);

        validateDuplicateForUpdate(id, request);

        organizationMapper.updateEntity(
                request,
                organization
        );

        Organization updated =
                organizationRepository.save(organization);

        return organizationMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {

        Organization organization =
                findOrganization(id);

        organization.setActive(false);

        organizationRepository.save(organization);
    }

    @Override
    public void activate(Long id) {

        Organization organization =
                organizationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organization",
                                        id
                                )
                        );

        organization.setActive(true);

        organizationRepository.save(organization);
    }

    @Override
    public void deactivate(Long id) {

        Organization organization =
                findOrganization(id);

        organization.setActive(false);

        organizationRepository.save(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActive() {

        return organizationRepository.countByActiveTrue();
    }

    private Organization findOrganization(Long id) {

        return organizationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Organization",
                                id
                        )
                );
    }

    private void validateDuplicateForCreate(
            OrganizationRequest request) {

        if (organizationRepository
                .existsByCodeIgnoreCase(request.getCode())) {

            throw new DuplicateResourceException(
                    "Organization",
                    "code",
                    request.getCode()
            );
        }

        if (organizationRepository
                .existsByEmailIgnoreCase(request.getEmail())) {

            throw new DuplicateResourceException(
                    "Organization",
                    "email",
                    request.getEmail()
            );
        }
    }

    private void validateDuplicateForUpdate(
            Long id,
            OrganizationUpdateRequest request) {

        if (organizationRepository
                .existsByCodeIgnoreCaseAndIdNot(
                        request.getCode(),
                        id)) {

            throw new DuplicateResourceException(
                    "Organization",
                    "code",
                    request.getCode()
            );
        }

        if (organizationRepository
                .existsByEmailIgnoreCaseAndIdNot(
                        request.getEmail(),
                        id)) {

            throw new DuplicateResourceException(
                    "Organization",
                    "email",
                    request.getEmail()
            );
        }
    }
}