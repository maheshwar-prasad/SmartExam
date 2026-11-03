package com.smartexam.classroom.service.impl;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.classroom.dto.ClassRoomRequest;
import com.smartexam.classroom.dto.ClassRoomResponse;
import com.smartexam.classroom.dto.ClassRoomSearchRequest;
import com.smartexam.classroom.dto.ClassRoomUpdateRequest;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.classroom.mapper.ClassRoomMapper;
import com.smartexam.classroom.repository.ClassRoomRepository;
import com.smartexam.classroom.service.ClassRoomService;
import com.smartexam.classroom.specification.ClassRoomSpecification;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
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
public class ClassRoomServiceImpl implements ClassRoomService {

    private final ClassRoomRepository repository;

    private final ClassRoomMapper mapper;

    private final OrganizationRepository organizationRepository;

    private final AcademicSessionRepository academicSessionRepository;

    @Override
    public ClassRoomResponse create(ClassRoomRequest request) {

        log.info("Creating ClassRoom : {}", request.getName());

        if (repository.existsByCodeIgnoreCaseAndOrganizationId(
                request.getCode(),
                request.getOrganizationId())) {

            throw new DuplicateResourceException(
                    "ClassRoom Code",
                    request.getCode());
        }

        if (repository.existsByNameIgnoreCaseAndOrganizationId(
                request.getName(),
                request.getOrganizationId())) {

            throw new DuplicateResourceException(
                    "ClassRoom Name",
                    request.getName());
        }

        Organization organization =
                organizationRepository.findById(
                                request.getOrganizationId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organization",
                                        request.getOrganizationId()));

        AcademicSession academicSession =
                academicSessionRepository.findById(
                                request.getAcademicSessionId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Academic Session",
                                        request.getAcademicSessionId()));

        ClassRoom entity = mapper.toEntity(request);

        entity.setOrganization(organization);

        entity.setAcademicSession(academicSession);

        entity.setActive(true);

        ClassRoom saved = repository.save(entity);

        log.info("ClassRoom Created : {}", saved.getId());

        return mapper.toResponse(saved);

    }

    @Override
    public ClassRoomResponse update(
            Long id,
            ClassRoomUpdateRequest request) {

        log.info("Updating ClassRoom : {}", id);

        ClassRoom entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "ClassRoom",
                                        id));

        if (request.getCode() != null &&
                repository.existsByCodeIgnoreCaseAndOrganizationIdAndIdNot(
                        request.getCode(),
                        entity.getOrganization().getId(),
                        id)) {

            throw new DuplicateResourceException(
                    "ClassRoom Code",
                    request.getCode());

        }

        if (request.getName() != null &&
                repository.existsByNameIgnoreCaseAndOrganizationIdAndIdNot(
                        request.getName(),
                        entity.getOrganization().getId(),
                        id)) {

            throw new DuplicateResourceException(
                    "ClassRoom Name",
                    request.getName());

        }

        if (request.getAcademicSessionId() != null) {

            AcademicSession session =
                    academicSessionRepository.findById(
                                    request.getAcademicSessionId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Academic Session",
                                            request.getAcademicSessionId()));

            entity.setAcademicSession(session);

        }

        mapper.updateEntity(request, entity);

        ClassRoom updated = repository.save(entity);

        return mapper.toResponse(updated);

    }

    @Override
    @Transactional(readOnly = true)
    public ClassRoomResponse getById(Long id) {

        ClassRoom entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "ClassRoom",
                                id));

        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassRoomResponse> getAll() {

        return mapper.toResponseList(
                repository.findAll());

    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassRoomResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort);

        return repository.findAll(pageable)
                .map(mapper::toResponse);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassRoomResponse> search(
            ClassRoomSearchRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                request.getDirection().equalsIgnoreCase("DESC")
                        ? Sort.by(request.getSortBy()).descending()
                        : Sort.by(request.getSortBy()).ascending());

        Specification<ClassRoom> specification =
                (root, query, cb) -> cb.conjunction();

        specification = specification.and(
                ClassRoomSpecification.hasKeyword(
                        request.getKeyword()));

        specification = specification.and(
                ClassRoomSpecification.hasOrganization(
                        request.getOrganizationId()));

        specification = specification.and(
                ClassRoomSpecification.hasAcademicSession(
                        request.getAcademicSessionId()));

        specification = specification.and(
                ClassRoomSpecification.isActive(
                        request.getActive()));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);

    }

    @Override
    public ClassRoomResponse activate(Long id) {

        ClassRoom entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "ClassRoom",
                                id));

        entity.setActive(true);

        ClassRoom updated = repository.save(entity);

        log.info("ClassRoom Activated : {}", id);

        return mapper.toResponse(updated);

    }

    @Override
    public ClassRoomResponse deactivate(Long id) {

        ClassRoom entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "ClassRoom",
                                id));

        entity.setActive(false);

        ClassRoom updated = repository.save(entity);

        log.info("ClassRoom Deactivated : {}", id);

        return mapper.toResponse(updated);

    }

    @Override
    public void delete(Long id) {

        ClassRoom entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "ClassRoom",
                                id));

        entity.setActive(false);

        repository.save(entity);

        log.info("ClassRoom Soft Deleted : {}", id);

    }
}