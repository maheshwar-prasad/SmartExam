package com.smartexam.academic.service.impl;

import com.smartexam.academic.dto.AcademicSessionRequest;
import com.smartexam.academic.dto.AcademicSessionResponse;
import com.smartexam.academic.dto.AcademicSessionSearchRequest;
import com.smartexam.academic.dto.AcademicSessionUpdateRequest;
import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.mapper.AcademicSessionMapper;
import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.academic.service.AcademicSessionService;
import com.smartexam.academic.specification.AcademicSessionSpecification;
import com.smartexam.common.exception.BusinessException;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AcademicSessionServiceImpl implements AcademicSessionService {

    private final AcademicSessionRepository repository;
    private final AcademicSessionMapper mapper;
    private final OrganizationRepository organizationRepository;

    @Override
    public AcademicSessionResponse create(AcademicSessionRequest request) {

        log.info("Creating Academic Session : {}", request.getName());

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BusinessException(
                    "Start date must be before end date.");
        }

        if (repository.existsByCodeIgnoreCaseAndOrganizationId(
                request.getCode(),
                request.getOrganizationId())) {

            throw new DuplicateResourceException(
                    "Academic Session",
                    request.getCode());
        }

        Organization organization = organizationRepository.findById(
                        request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Organization",
                        request.getOrganizationId()));

        List<AcademicSession> overlap =
                repository.findOverlappingSessions(
                        request.getOrganizationId(),
                        request.getStartDate(),
                        request.getEndDate());

        if (!overlap.isEmpty()) {
            throw new BusinessException(
                    "Academic session dates overlap with an existing session.");
        }

        if (Boolean.TRUE.equals(request.getCurrentSession())) {

            repository.findByOrganizationIdAndCurrentSessionTrue(
                            request.getOrganizationId())
                    .ifPresent(session -> {
                        session.setCurrentSession(false);
                        repository.save(session);
                    });
        }

        AcademicSession entity = mapper.toEntity(request);

        entity.setOrganization(organization);
        entity.setActive(true);

        AcademicSession saved = repository.save(entity);

        return mapper.toResponse(saved);
    }

    @Override
    public AcademicSessionResponse update(
            Long id,
            AcademicSessionUpdateRequest request) {

        AcademicSession session = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Academic Session",
                                id));

        if (request.getStartDate() != null
                && request.getEndDate() != null
                && request.getStartDate().isAfter(request.getEndDate())) {

            throw new BusinessException(
                    "Start date must be before end date.");
        }

        if (request.getCode() != null &&
                repository.existsByCodeIgnoreCaseAndOrganizationIdAndIdNot(
                        request.getCode(),
                        session.getOrganization().getId(),
                        id)) {

            throw new DuplicateResourceException(
                    "Academic Session",
                    request.getCode());
        }

        if (request.getStartDate() != null &&
                request.getEndDate() != null) {

            List<AcademicSession> overlap =
                    repository.findOverlappingSessionsForUpdate(
                            id,
                            session.getOrganization().getId(),
                            request.getStartDate(),
                            request.getEndDate());

            if (!overlap.isEmpty()) {
                throw new BusinessException(
                        "Academic session dates overlap with another session.");
            }
        }

        if (Boolean.TRUE.equals(request.getCurrentSession())) {

            repository.findByOrganizationIdAndCurrentSessionTrue(
                            session.getOrganization().getId())
                    .ifPresent(existing -> {

                        if (!existing.getId().equals(id)) {

                            existing.setCurrentSession(false);

                            repository.save(existing);
                        }

                    });

        }

        mapper.updateEntity(request, session);

        AcademicSession updated = repository.save(session);

        return mapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicSessionResponse getById(Long id) {

        AcademicSession entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Academic Session",
                                id));

        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicSessionResponse> getAll() {

        return mapper.toResponseList(repository.findAll());

    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcademicSessionResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(pageable)
                .map(mapper::toResponse);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcademicSessionResponse> search(
            AcademicSessionSearchRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                request.getDirection().equalsIgnoreCase("DESC")
                        ? Sort.by(request.getSortBy()).descending()
                        : Sort.by(request.getSortBy()).ascending());

        Specification<AcademicSession> specification =
                (root, query, cb) -> cb.conjunction();

        specification = specification.and(
                AcademicSessionSpecification.hasKeyword(request.getKeyword()));

        specification = specification.and(
                AcademicSessionSpecification.hasOrganization(
                        request.getOrganizationId()));

        specification = specification.and(
                AcademicSessionSpecification.isCurrent(
                        request.getCurrentSession()));

        specification = specification.and(
                AcademicSessionSpecification.isActive(
                        request.getActive()));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public AcademicSessionResponse activate(Long id) {

        AcademicSession session = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Academic Session",
                                id));

        session.setActive(true);

        return mapper.toResponse(repository.save(session));
    }

    @Override
    public AcademicSessionResponse deactivate(Long id) {

        AcademicSession session = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Academic Session",
                                id));

        session.setActive(false);

        return mapper.toResponse(repository.save(session));
    }

    @Override
    public void delete(Long id) {

        AcademicSession session = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Academic Session",
                                id));

        session.setActive(false);

        repository.save(session);

        log.info("Academic Session Soft Deleted : {}", id);
    }
}