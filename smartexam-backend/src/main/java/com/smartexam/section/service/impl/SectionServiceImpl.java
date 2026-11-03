package com.smartexam.section.service.impl;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.classroom.repository.ClassRoomRepository;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.section.dto.SectionRequest;
import com.smartexam.section.dto.SectionResponse;
import com.smartexam.section.dto.SectionSearchRequest;
import com.smartexam.section.dto.SectionUpdateRequest;
import com.smartexam.section.entity.Section;
import com.smartexam.section.mapper.SectionMapper;
import com.smartexam.section.repository.SectionRepository;
import com.smartexam.section.service.SectionService;
import com.smartexam.section.specification.SectionSpecification;
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
public class SectionServiceImpl implements SectionService {

    private final SectionRepository repository;

    private final SectionMapper mapper;

    private final OrganizationRepository organizationRepository;

    private final AcademicSessionRepository academicSessionRepository;

    private final ClassRoomRepository classRoomRepository;

    @Override
    public SectionResponse create(
            SectionRequest request) {

        log.info("Creating Section : {}", request.getName());

        if (repository.existsByCodeIgnoreCaseAndOrganizationId(
                request.getCode(),
                request.getOrganizationId())) {

            throw new DuplicateResourceException(
                    "Section Code",
                    request.getCode());
        }

        if (repository.existsByNameIgnoreCaseAndClassRoomId(
                request.getName(),
                request.getClassRoomId())) {

            throw new DuplicateResourceException(
                    "Section Name",
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

        ClassRoom classRoom =
                classRoomRepository.findById(
                                request.getClassRoomId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "ClassRoom",
                                        request.getClassRoomId()));

        Section entity = mapper.toEntity(request);

        entity.setOrganization(organization);

        entity.setAcademicSession(session);

        entity.setClassRoom(classRoom);

        entity.setActive(true);

        Section saved = repository.save(entity);

        log.info("Section Created Successfully : {}", saved.getId());

        return mapper.toResponse(saved);

    }

    @Override
    public SectionResponse update(
            Long id,
            SectionUpdateRequest request) {

        log.info("Updating Section : {}", id);

        Section entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section",
                                id));

        if (request.getCode() != null &&
                repository.existsByCodeIgnoreCaseAndOrganizationIdAndIdNot(
                        request.getCode(),
                        entity.getOrganization().getId(),
                        id)) {

            throw new DuplicateResourceException(
                    "Section Code",
                    request.getCode());

        }

        if (request.getName() != null &&
                repository.existsByNameIgnoreCaseAndClassRoomIdAndIdNot(
                        request.getName(),
                        entity.getClassRoom().getId(),
                        id)) {

            throw new DuplicateResourceException(
                    "Section Name",
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

        if (request.getClassRoomId() != null) {

            ClassRoom classRoom =
                    classRoomRepository.findById(
                                    request.getClassRoomId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "ClassRoom",
                                            request.getClassRoomId()));

            entity.setClassRoom(classRoom);

        }

        mapper.updateEntity(request, entity);

        Section updated = repository.save(entity);

        return mapper.toResponse(updated);

    }

    @Override
    @Transactional(readOnly = true)
    public SectionResponse getById(Long id) {

        Section entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section",
                                id));

        return mapper.toResponse(entity);

    }

    @Override
    @Transactional(readOnly = true)
    public List<SectionResponse> getAll() {

        return mapper.toResponseList(
                repository.findAll());

    }

    @Override
    @Transactional(readOnly = true)
    public Page<SectionResponse> getAll(
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
    public Page<SectionResponse> search(
            SectionSearchRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                request.getDirection().equalsIgnoreCase("DESC")
                        ? Sort.by(request.getSortBy()).descending()
                        : Sort.by(request.getSortBy()).ascending());

        Specification<Section> specification =
                (root, query, cb) -> cb.conjunction();

        specification = specification.and(
                SectionSpecification.hasKeyword(
                        request.getKeyword()));

        specification = specification.and(
                SectionSpecification.hasOrganization(
                        request.getOrganizationId()));

        specification = specification.and(
                SectionSpecification.hasAcademicSession(
                        request.getAcademicSessionId()));

        specification = specification.and(
                SectionSpecification.hasClassRoom(
                        request.getClassRoomId()));

        specification = specification.and(
                SectionSpecification.isActive(
                        request.getActive()));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);

    }

    @Override
    public SectionResponse activate(Long id) {

        Section entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section",
                                id));

        entity.setActive(true);

        Section updated = repository.save(entity);

        log.info("Section Activated : {}", id);

        return mapper.toResponse(updated);

    }

    @Override
    public SectionResponse deactivate(Long id) {

        Section entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section",
                                id));

        entity.setActive(false);

        Section updated = repository.save(entity);

        log.info("Section Deactivated : {}", id);

        return mapper.toResponse(updated);

    }

    @Override
    public void delete(Long id) {

        Section entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section",
                                id));

        entity.setActive(false);

        repository.save(entity);

        log.info("Section Soft Deleted : {}", id);

    }
}