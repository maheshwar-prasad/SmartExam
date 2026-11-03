package com.smartexam.subject.service.impl;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.common.exception.BusinessException;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.subject.dto.*;
import com.smartexam.subject.entity.Subject;
import com.smartexam.subject.mapper.SubjectMapper;
import com.smartexam.subject.repository.SubjectRepository;
import com.smartexam.subject.service.SubjectService;
import com.smartexam.subject.specification.SubjectSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository repository;
    private final SubjectMapper mapper;
    private final OrganizationRepository organizationRepository;
    private final AcademicSessionRepository academicSessionRepository;

    @Override
    public SubjectResponse create(SubjectRequest request) {

        log.info("Creating Subject : {}", request.getCode());

        validateMarks(request.getTheoryMarks(),
                request.getPracticalMarks(),
                request.getPassMarks());

        if (repository.existsByCodeIgnoreCaseAndOrganizationId(
                request.getCode(),
                request.getOrganizationId())) {

            throw new DuplicateResourceException(
                    "Subject Code",
                    request.getCode());
        }

        if (repository.existsByNameIgnoreCaseAndOrganizationIdAndAcademicSessionId(
                request.getName(),
                request.getOrganizationId(),
                request.getAcademicSessionId())) {

            throw new DuplicateResourceException(
                    "Subject",
                    request.getName());
        }

        Organization organization =
                organizationRepository.findById(
                                request.getOrganizationId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organization",
                                        request.getOrganizationId()));

        AcademicSession session =
                academicSessionRepository.findById(
                                request.getAcademicSessionId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Academic Session",
                                        request.getAcademicSessionId()));

        if (!organization.getId().equals(
                session.getOrganization().getId())) {

            throw new BusinessException(
                    "Academic Session does not belong to Organization.");
        }

        Subject entity = mapper.toEntity(request);

        entity.setOrganization(organization);
        entity.setAcademicSession(session);
        entity.setActive(true);

        Subject saved = repository.save(entity);

        log.info("Subject Created Successfully : {}", saved.getId());

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectResponse getById(Long id) {

        Subject subject = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject", id));

        return mapper.toResponse(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectResponse> getAll() {

        return mapper.toResponseList(
                repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubjectResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort =
                direction.equalsIgnoreCase("DESC")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubjectResponse> search(
            SubjectSearchRequest request) {

        Pageable pageable =
                PageRequest.of(
                        request.getPage(),
                        request.getSize(),
                        request.getDirection().equalsIgnoreCase("DESC")
                                ? Sort.by(request.getSortBy()).descending()
                                : Sort.by(request.getSortBy()).ascending());

        Specification<Subject> specification =
                SubjectSpecification.hasKeyword(request.getKeyword())
                        .and(SubjectSpecification.hasOrganization(request.getOrganizationId()))
                        .and(SubjectSpecification.hasAcademicSession(request.getAcademicSessionId()))
                        .and(SubjectSpecification.isActive(request.getActive()));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public SubjectResponse activate(Long id) {

        Subject subject = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subject", id));

        subject.setActive(true);

        return mapper.toResponse(repository.save(subject));
    }

    @Override
    public SubjectResponse deactivate(Long id) {

        Subject subject = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subject", id));

        subject.setActive(false);

        return mapper.toResponse(repository.save(subject));
    }

    @Override
    public void delete(Long id) {

        Subject subject = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subject", id));

        subject.setActive(false);

        repository.save(subject);

        log.info("Subject Soft Deleted : {}", id);
    }

    private void validateMarks(Integer theory,
            Integer practical,
            Integer pass) {

        int total = theory + practical;

        if (pass > total) {

            throw new BusinessException(
                    "Pass marks cannot be greater than total marks.");
        }
    }
    @Override
    public SubjectResponse update(
            Long id,
            SubjectUpdateRequest request) {

        Subject subject = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject",
                                id));

        Integer theoryMarks =
                request.getTheoryMarks() != null
                        ? request.getTheoryMarks()
                        : subject.getTheoryMarks();

        Integer practicalMarks =
                request.getPracticalMarks() != null
                        ? request.getPracticalMarks()
                        : subject.getPracticalMarks();

        Integer passMarks =
                request.getPassMarks() != null
                        ? request.getPassMarks()
                        : subject.getPassMarks();

        validateMarks(
                theoryMarks,
                practicalMarks,
                passMarks);
        Long organizationId =
                request.getOrganizationId() != null
                        ? request.getOrganizationId()
                        : subject.getOrganization().getId();
        Long academicSessionId =
                request.getAcademicSessionId() != null
                        ? request.getAcademicSessionId()
                        : subject.getAcademicSession().getId();
        if (request.getCode() != null &&
                repository.existsByCodeIgnoreCaseAndOrganizationIdAndIdNot(
                        request.getCode(),
                        organizationId,
                        id)) {
            throw new DuplicateResourceException(
                    "Subject Code",
                    request.getCode());
        }
        if (request.getName() != null &&
                repository.existsByNameIgnoreCaseAndOrganizationIdAndAcademicSessionIdAndIdNot(
                        request.getName(),
                        organizationId,
                        academicSessionId,
                        id)) {
            throw new DuplicateResourceException(
                    "Subject",
                    request.getName());
        }
        if (request.getOrganizationId() != null) {
            Organization organization =
                    organizationRepository.findById(
                                    request.getOrganizationId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Organization",
                                            request.getOrganizationId()));

            subject.setOrganization(organization);
        }
        if (request.getAcademicSessionId() != null) {
            AcademicSession session =
                    academicSessionRepository.findById(
                                    request.getAcademicSessionId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Academic Session",
                                            request.getAcademicSessionId()));
            Long orgId =
                    subject.getOrganization().getId();
            if (!session.getOrganization().getId().equals(orgId)) {

                throw new BusinessException(
                        "Academic Session does not belong to Organization.");
            }
            subject.setAcademicSession(session);
        }
        mapper.updateEntity(
                request,
                subject);
        Subject updated =
                repository.save(subject);
        log.info(
                "Subject Updated Successfully : {}",
                updated.getId());
        return mapper.toResponse(updated);
    }
}