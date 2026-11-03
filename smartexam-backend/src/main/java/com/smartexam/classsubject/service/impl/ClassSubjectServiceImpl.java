package com.smartexam.classsubject.service.impl;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.classroom.repository.ClassRoomRepository;
import com.smartexam.classsubject.dto.ClassSubjectRequest;
import com.smartexam.classsubject.dto.ClassSubjectResponse;
import com.smartexam.classsubject.dto.ClassSubjectSearchRequest;
import com.smartexam.classsubject.dto.ClassSubjectUpdateRequest;
import com.smartexam.classsubject.entity.ClassSubject;
import com.smartexam.classsubject.mapper.ClassSubjectMapper;
import com.smartexam.classsubject.repository.ClassSubjectRepository;
import com.smartexam.classsubject.service.ClassSubjectService;
import com.smartexam.classsubject.specification.ClassSubjectSpecification;

import com.smartexam.common.exception.BusinessException;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.subject.entity.Subject;
import com.smartexam.subject.repository.SubjectRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassSubjectServiceImpl
        implements ClassSubjectService {

    private final ClassSubjectRepository classSubjectRepository;

    private final ClassSubjectMapper classSubjectMapper;

    /*
     * IMPORTANT:
     *
     * Replace these repository types/imports with the
     * repositories already present in your project.
     */
    private final OrganizationRepository organizationRepository;

    private final AcademicSessionRepository academicSessionRepository;

    private final ClassRoomRepository classRoomRepository;

    private final SubjectRepository subjectRepository;


    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public ClassSubjectResponse create(
            ClassSubjectRequest request
    ) {

        validateRequest(request);

        var organization =
                organizationRepository
                        .findById(request.organizationId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Organization not found."
                                )
                        );

        var academicSession =
                academicSessionRepository
                        .findById(request.academicSessionId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Academic Session not found."
                                )
                        );

        var classRoom =
                classRoomRepository
                        .findById(request.classRoomId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Class not found."
                                )
                        );

        var subject =
                subjectRepository
                        .findById(request.subjectId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Subject not found."
                                )
                        );

        validateOrganizationConsistency(
                organization,
                academicSession,
                classRoom,
                subject
        );

        boolean exists =
                classSubjectRepository
                        .existsByOrganizationIdAndAcademicSessionIdAndClassRoomIdAndSubjectId(
                                request.organizationId(),
                                request.academicSessionId(),
                                request.classRoomId(),
                                request.subjectId()
                        );

        if (exists) {
            throw new BusinessException(
                    "This subject is already assigned to this class for the selected academic session."
            );
        }

        ClassSubject entity =
                ClassSubject.builder()
                        .organization(organization)
                        .academicSession(academicSession)
                        .classRoom(classRoom)
                        .subject(subject)
                        .active(
                                request.active() == null
                                        || request.active()
                        )
                        .build();

        ClassSubject saved =
                classSubjectRepository.save(entity);

        return classSubjectMapper.toResponse(saved);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public ClassSubjectResponse update(
            Long id,
            ClassSubjectUpdateRequest request
    ) {

        if (id == null) {
            throw new BusinessException(
                    "Class Subject ID is required."
            );
        }

        validateRequest(request);

        ClassSubject entity =
                classSubjectRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Class Subject not found."
                                )
                        );

        var organization =
                organizationRepository
                        .findById(request.organizationId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Organization not found."
                                )
                        );

        var academicSession =
                academicSessionRepository
                        .findById(request.academicSessionId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Academic Session not found."
                                )
                        );

        var classRoom =
                classRoomRepository
                        .findById(request.classRoomId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Class not found."
                                )
                        );

        var subject =
                subjectRepository
                        .findById(request.subjectId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Subject not found."
                                )
                        );

        validateOrganizationConsistency(
                organization,
                academicSession,
                classRoom,
                subject
        );

        boolean exists =
                classSubjectRepository
                        .existsByOrganizationIdAndAcademicSessionIdAndClassRoomIdAndSubjectIdAndIdNot(
                                request.organizationId(),
                                request.academicSessionId(),
                                request.classRoomId(),
                                request.subjectId(),
                                id
                        );

        if (exists) {
            throw new BusinessException(
                    "This subject is already assigned to this class for the selected academic session."
            );
        }

        entity.setOrganization(organization);
        entity.setAcademicSession(academicSession);
        entity.setClassRoom(classRoom);
        entity.setSubject(subject);

        if (request.active() != null) {
            entity.setActive(request.active());
        }

        ClassSubject updated =
                classSubjectRepository.save(entity);

        return classSubjectMapper.toResponse(updated);
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional
    public ClassSubjectResponse getById(
            Long id
    ) {

        ClassSubject entity =
                classSubjectRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Class Subject not found."
                                )
                        );

        return classSubjectMapper.toResponse(entity);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional
    public List<ClassSubjectResponse> getAll() {

        return classSubjectRepository
                .findAll(
                        Sort.by(
                                Sort.Direction.ASC,
                                "id"
                        )
                )
                .stream()
                .map(classSubjectMapper::toResponse)
                .toList();
    }


    // =========================================================
    // PAGINATION
    // =========================================================

    @Override
    @Transactional
    public Page<ClassSubjectResponse> getPage(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort.Direction sortDirection =
                "DESC".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        String safeSortBy =
                sortBy == null ||
                        sortBy.isBlank()
                        ? "id"
                        : sortBy;

        Pageable pageable =
                PageRequest.of(
                        Math.max(page, 0),
                        Math.max(size, 1),
                        Sort.by(
                                sortDirection,
                                safeSortBy
                        )
                );

        return classSubjectRepository
                .findAll(pageable)
                .map(classSubjectMapper::toResponse);
    }


    // =========================================================
    // SEARCH
    // =========================================================

    @Override
    @Transactional
    public Page<ClassSubjectResponse> search(
            ClassSubjectSearchRequest request
    ) {

        int page =
                request.page() == null
                        ? 0
                        : Math.max(request.page(), 0);

        int size =
                request.size() == null
                        ? 10
                        : Math.max(request.size(), 1);

        String sortBy =
                request.sortBy() == null ||
                        request.sortBy().isBlank()
                        ? "id"
                        : request.sortBy();

        Sort.Direction direction =
                "DESC".equalsIgnoreCase(
                        request.direction()
                )
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                direction,
                                sortBy
                        )
                );

        Specification<ClassSubject> specification =
                Specification
                        .where(
                                ClassSubjectSpecification.keyword(
                                        request.keyword()
                                )
                        )
                        .and(
                                ClassSubjectSpecification.organizationId(
                                        request.organizationId()
                                )
                        )
                        .and(
                                ClassSubjectSpecification.academicSessionId(
                                        request.academicSessionId()
                                )
                        )
                        .and(
                                ClassSubjectSpecification.classRoomId(
                                        request.classRoomId()
                                )
                        )
                        .and(
                                ClassSubjectSpecification.subjectId(
                                        request.subjectId()
                                )
                        )
                        .and(
                                ClassSubjectSpecification.active(
                                        request.active()
                                )
                        );

        return classSubjectRepository
                .findAll(
                        specification,
                        pageable
                )
                .map(classSubjectMapper::toResponse);
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @Override
    public ClassSubjectResponse activate(
            Long id
    ) {

        ClassSubject entity =
                classSubjectRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Class Subject not found."
                                )
                        );

        entity.setActive(true);

        return classSubjectMapper.toResponse(
                classSubjectRepository.save(entity)
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Override
    public ClassSubjectResponse deactivate(
            Long id
    ) {

        ClassSubject entity =
                classSubjectRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Class Subject not found."
                                )
                        );

        entity.setActive(false);

        return classSubjectMapper.toResponse(
                classSubjectRepository.save(entity)
        );
    }


    // =========================================================
    // DELETE / SOFT DELETE
    // =========================================================

    @Override
    public void delete(
            Long id
    ) {

        ClassSubject entity =
                classSubjectRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Class Subject not found."
                                )
                        );

        entity.setActive(false);

        classSubjectRepository.save(entity);
    }


    // =========================================================
    // VALIDATION
    // =========================================================

    private void validateRequest(
            ClassSubjectRequest request
    ) {

        if (request == null) {
            throw new BusinessException(
                    "Request cannot be null."
            );
        }

        if (request.organizationId() == null) {
            throw new BusinessException(
                    "Organization is required."
            );
        }

        if (request.academicSessionId() == null) {
            throw new BusinessException(
                    "Academic Session is required."
            );
        }

        if (request.classRoomId() == null) {
            throw new BusinessException(
                    "Class is required."
            );
        }

        if (request.subjectId() == null) {
            throw new BusinessException(
                    "Subject is required."
            );
        }
    }


    private void validateRequest(
            ClassSubjectUpdateRequest request
    ) {

        if (request == null) {
            throw new BusinessException(
                    "Request cannot be null."
            );
        }

        if (request.organizationId() == null) {
            throw new BusinessException(
                    "Organization is required."
            );
        }

        if (request.academicSessionId() == null) {
            throw new BusinessException(
                    "Academic Session is required."
            );
        }

        if (request.classRoomId() == null) {
            throw new BusinessException(
                    "Class is required."
            );
        }

        if (request.subjectId() == null) {
            throw new BusinessException(
                    "Subject is required."
            );
        }
    }


    // =========================================================
    // ORGANIZATION CONSISTENCY
    // =========================================================

    private void validateOrganizationConsistency(
            Organization organization,
            AcademicSession academicSession,
            ClassRoom classRoom,
            Subject subject
    ) {

        Long organizationId =
                organization.getId();

        if (!organizationId.equals(
                getOrganizationId(academicSession)
        )) {
            throw new BusinessException(
                    "Academic Session does not belong to Organization."
            );
        }

        if (!organizationId.equals(
                getOrganizationId(classRoom)
        )) {
            throw new BusinessException(
                    "Class does not belong to Organization."
            );
        }

        if (!organizationId.equals(
                getOrganizationId(subject)
        )) {
            throw new BusinessException(
                    "Subject does not belong to Organization."
            );
        }

        if (classRoom.getAcademicSession() != null) {

            Long classSessionId =
                    classRoom
                            .getAcademicSession()
                            .getId();

            if (!classSessionId.equals(
                    academicSession.getId()
            )) {
                throw new BusinessException(
                        "Class does not belong to the selected Academic Session."
                );
            }
        }

        if (subject.getAcademicSession() != null) {

            Long subjectSessionId =
                    subject
                            .getAcademicSession()
                            .getId();

            if (!subjectSessionId.equals(
                    academicSession.getId()
            )) {
                throw new BusinessException(
                        "Subject does not belong to the selected Academic Session."
                );
            }
        }
    }


    // =========================================================
    // ORGANIZATION HELPERS
    // =========================================================

    private Long getOrganizationId(
            AcademicSession academicSession
    ) {

        return academicSession
                .getOrganization()
                .getId();
    }


    private Long getOrganizationId(
            ClassRoom classRoom
    ) {

        return classRoom
                .getOrganization()
                .getId();
    }


    private Long getOrganizationId(
            Subject subject
    ) {

        return subject
                .getOrganization()
                .getId();
    }
}