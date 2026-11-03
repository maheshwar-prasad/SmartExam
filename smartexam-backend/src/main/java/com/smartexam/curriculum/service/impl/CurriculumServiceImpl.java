package com.smartexam.curriculum.service.impl;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.classroom.repository.ClassRoomRepository;
import com.smartexam.classsubject.entity.ClassSubject;
import com.smartexam.classsubject.repository.ClassSubjectRepository;
import com.smartexam.curriculum.dto.CurriculumRequest;
import com.smartexam.curriculum.dto.CurriculumResponse;
import com.smartexam.curriculum.dto.CurriculumSubjectRequest;
import com.smartexam.curriculum.dto.CurriculumSubjectResponse;
import com.smartexam.curriculum.dto.CurriculumUpdateRequest;
import com.smartexam.curriculum.entity.Curriculum;
import com.smartexam.curriculum.entity.CurriculumSubject;
import com.smartexam.curriculum.mapper.CurriculumMapper;
import com.smartexam.curriculum.repository.CurriculumRepository;
import com.smartexam.curriculum.repository.CurriculumSubjectRepository;
import com.smartexam.curriculum.service.CurriculumService;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// IMPORTANT:
// Replace these three imports with the exact package names
// already used in your project.

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CurriculumServiceImpl
        implements CurriculumService {

    private final CurriculumRepository curriculumRepository;

    private final CurriculumSubjectRepository
            curriculumSubjectRepository;

    private final CurriculumMapper curriculumMapper;

    private final OrganizationRepository organizationRepository;

    private final AcademicSessionRepository
            academicSessionRepository;

    private final ClassRoomRepository classRoomRepository;

    private final ClassSubjectRepository
            classSubjectRepository;


    // ============================================================
    // CREATE
    // ============================================================

    @Override
    public CurriculumResponse create(
            CurriculumRequest request
    ) {

        validateCreateRequest(request);

        if (
                curriculumRepository
                        .existsByOrganizationIdAndAcademicSessionIdAndClassRoomIdAndCode(
                                request.organizationId(),
                                request.academicSessionId(),
                                request.classRoomId(),
                                request.code().trim()
                        )
        ) {

            throw new IllegalArgumentException(
                    "Curriculum with the same code already exists for this class and academic session."
            );
        }


        Organization organization =
                organizationRepository
                        .findById(request.organizationId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Organization not found: "
                                                + request.organizationId()
                                )
                        );


        AcademicSession academicSession =
                academicSessionRepository
                        .findById(request.academicSessionId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Academic session not found: "
                                                + request.academicSessionId()
                                )
                        );


        ClassRoom classRoom =
                classRoomRepository
                        .findById(request.classRoomId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Class not found: "
                                                + request.classRoomId()
                                )
                        );


        validateOrganization(
                organization.getId(),
                request.organizationId()
        );


        Curriculum curriculum =
                Curriculum.builder()

                        .organization(organization)

                        .academicSession(
                                academicSession
                        )

                        .classRoom(
                                classRoom
                        )

                        .name(
                                request.name().trim()
                        )

                        .code(
                                request.code().trim()
                        )

                        .description(
                                request.description() != null
                                        ? request.description().trim()
                                        : null
                        )

                        .build();


        curriculum.setActive(
                request.active() == null
                        || request.active()
        );


        Curriculum saved =
                curriculumRepository.save(
                        curriculum
                );


        return curriculumMapper.toResponse(
                saved
        );
    }


    // ============================================================
    // UPDATE
    // ============================================================

    @Override
    public CurriculumResponse update(
            Long id,
            CurriculumUpdateRequest request
    ) {

        Curriculum curriculum =
                curriculumRepository
                        .findByIdAndOrganizationId(
                                id,
                                request.organizationId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum not found: " + id
                                )
                        );


        if (
                curriculumRepository
                        .existsByOrganizationIdAndAcademicSessionIdAndClassRoomIdAndCodeAndIdNot(
                                request.organizationId(),
                                request.academicSessionId(),
                                request.classRoomId(),
                                request.code().trim(),
                                id
                        )
        ) {

            throw new IllegalArgumentException(
                    "Another curriculum with the same code already exists for this class and academic session."
            );
        }


        AcademicSession academicSession =
                academicSessionRepository
                        .findById(
                                request.academicSessionId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Academic session not found: "
                                                + request.academicSessionId()
                                )
                        );


        ClassRoom classRoom =
                classRoomRepository
                        .findById(
                                request.classRoomId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Class not found: "
                                                + request.classRoomId()
                                )
                        );


        curriculum.setAcademicSession(
                academicSession
        );

        curriculum.setClassRoom(
                classRoom
        );

        curriculum.setName(
                request.name().trim()
        );

        curriculum.setCode(
                request.code().trim()
        );

        curriculum.setDescription(
                request.description() != null
                        ? request.description().trim()
                        : null
        );

        if (request.active() != null) {

            curriculum.setActive(
                    request.active()
            );
        }


        Curriculum updated =
                curriculumRepository.save(
                        curriculum
                );


        return curriculumMapper.toResponse(
                updated
        );
    }


    // ============================================================
    // GET BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CurriculumResponse getById(
            Long id
    ) {

        Curriculum curriculum =
                curriculumRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum not found: " + id
                                )
                        );


        return curriculumMapper.toResponse(
                curriculum
        );
    }


    // ============================================================
    // GET ALL
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CurriculumResponse> getAll() {

        return curriculumRepository
                .findAll()
                .stream()
                .map(
                        curriculumMapper::toResponse
                )
                .toList();
    }


    // ============================================================
    // GET BY ORGANIZATION
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CurriculumResponse>
    getByOrganization(
            Long organizationId
    ) {

        return curriculumRepository
                .findByOrganizationId(
                        organizationId
                )
                .stream()
                .map(
                        curriculumMapper::toResponse
                )
                .toList();
    }


    // ============================================================
    // GET BY ACADEMIC SESSION
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CurriculumResponse>
    getByAcademicSession(
            Long academicSessionId
    ) {

        return curriculumRepository
                .findByAcademicSessionId(
                        academicSessionId
                )
                .stream()
                .map(
                        curriculumMapper::toResponse
                )
                .toList();
    }


    // ============================================================
    // GET BY CLASS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CurriculumResponse>
    getByClassRoom(
            Long classRoomId
    ) {

        return curriculumRepository
                .findByClassRoomId(
                        classRoomId
                )
                .stream()
                .map(
                        curriculumMapper::toResponse
                )
                .toList();
    }


    // ============================================================
    // GET BY SESSION + CLASS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CurriculumResponse>
    getBySessionAndClass(
            Long academicSessionId,
            Long classRoomId
    ) {

        return curriculumRepository
                .findByAcademicSessionIdAndClassRoomId(
                        academicSessionId,
                        classRoomId
                )
                .stream()
                .map(
                        curriculumMapper::toResponse
                )
                .toList();
    }


    // ============================================================
    // ACTIVATE
    // ============================================================

    @Override
    public CurriculumResponse activate(
            Long id
    ) {

        Curriculum curriculum =
                curriculumRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum not found: " + id
                                )
                        );


        curriculum.setActive(true);

        return curriculumMapper.toResponse(
                curriculumRepository.save(
                        curriculum
                )
        );
    }


    // ============================================================
    // DEACTIVATE
    // ============================================================

    @Override
    public CurriculumResponse deactivate(
            Long id
    ) {

        Curriculum curriculum =
                curriculumRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum not found: " + id
                                )
                        );


        curriculum.setActive(false);

        return curriculumMapper.toResponse(
                curriculumRepository.save(
                        curriculum
                )
        );
    }


    // ============================================================
    // DELETE CURRICULUM
    // ============================================================

    @Override
    public void delete(
            Long id
    ) {

        Curriculum curriculum =
                curriculumRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum not found: " + id
                                )
                        );


        curriculumRepository.delete(
                curriculum
        );
    }


    // ============================================================
    // ADD SUBJECT
    // ============================================================

    @Override
    public CurriculumSubjectResponse addSubject(
            Long curriculumId,
            CurriculumSubjectRequest request
    ) {

        Curriculum curriculum =
                curriculumRepository
                        .findById(curriculumId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum not found: "
                                                + curriculumId
                                )
                        );


        ClassSubject classSubject =
                classSubjectRepository
                        .findById(
                                request.classSubjectId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Class subject not found: "
                                                + request.classSubjectId()
                                )
                        );


        if (
                curriculumSubjectRepository
                        .existsByCurriculumIdAndClassSubjectId(
                                curriculumId,
                                request.classSubjectId()
                        )
        ) {

            throw new IllegalArgumentException(
                    "This subject is already part of the curriculum."
            );
        }


        CurriculumSubject curriculumSubject =
                CurriculumSubject.builder()

                        .curriculum(
                                curriculum
                        )

                        .classSubject(
                                classSubject
                        )

                        .sequenceNo(
                                request.sequenceNo()
                        )

                        .theoryMarks(
                                request.theoryMarks()
                        )

                        .practicalMarks(
                                request.practicalMarks()
                        )

                        .passingMarks(
                                request.passingMarks()
                        )

                        .build();


        curriculumSubject.setActive(
                request.active() == null
                        || request.active()
        );


        CurriculumSubject saved =
                curriculumSubjectRepository.save(
                        curriculumSubject
                );


        return curriculumMapper.toSubjectResponse(
                saved
        );
    }


    // ============================================================
    // UPDATE SUBJECT
    // ============================================================

    @Override
    public CurriculumSubjectResponse updateSubject(
            Long curriculumSubjectId,
            CurriculumSubjectRequest request
    ) {

        CurriculumSubject curriculumSubject =
                curriculumSubjectRepository
                        .findById(
                                curriculumSubjectId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum subject not found: "
                                                + curriculumSubjectId
                                )
                        );


        if (
                curriculumSubjectRepository
                        .existsByCurriculumIdAndClassSubjectIdAndIdNot(
                                curriculumSubject
                                        .getCurriculum()
                                        .getId(),

                                request.classSubjectId(),

                                curriculumSubjectId
                        )
        ) {

            throw new IllegalArgumentException(
                    "This subject is already part of the curriculum."
            );
        }


        ClassSubject classSubject =
                classSubjectRepository
                        .findById(
                                request.classSubjectId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Class subject not found: "
                                                + request.classSubjectId()
                                )
                        );


        curriculumSubject.setClassSubject(
                classSubject
        );

        curriculumSubject.setSequenceNo(
                request.sequenceNo()
        );

        curriculumSubject.setTheoryMarks(
                request.theoryMarks()
        );

        curriculumSubject.setPracticalMarks(
                request.practicalMarks()
        );

        curriculumSubject.setPassingMarks(
                request.passingMarks()
        );

        if (request.active() != null) {

            curriculumSubject.setActive(
                    request.active()
            );
        }


        CurriculumSubject updated =
                curriculumSubjectRepository.save(
                        curriculumSubject
                );


        return curriculumMapper.toSubjectResponse(
                updated
        );
    }


    // ============================================================
    // GET SUBJECTS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CurriculumSubjectResponse>
    getSubjects(
            Long curriculumId
    ) {

        return curriculumSubjectRepository
                .findByCurriculumId(
                        curriculumId
                )
                .stream()
                .sorted(
                        (a, b) ->
                                Integer.compare(
                                        a.getSequenceNo(),
                                        b.getSequenceNo()
                                )
                )
                .map(
                        curriculumMapper::toSubjectResponse
                )
                .toList();
    }


    // ============================================================
    // ACTIVATE SUBJECT
    // ============================================================

    @Override
    public CurriculumSubjectResponse
    activateSubject(
            Long curriculumSubjectId
    ) {

        CurriculumSubject subject =
                curriculumSubjectRepository
                        .findById(
                                curriculumSubjectId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum subject not found: "
                                                + curriculumSubjectId
                                )
                        );


        subject.setActive(true);

        return curriculumMapper.toSubjectResponse(
                curriculumSubjectRepository.save(
                        subject
                )
        );
    }


    // ============================================================
    // DEACTIVATE SUBJECT
    // ============================================================

    @Override
    public CurriculumSubjectResponse
    deactivateSubject(
            Long curriculumSubjectId
    ) {

        CurriculumSubject subject =
                curriculumSubjectRepository
                        .findById(
                                curriculumSubjectId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum subject not found: "
                                                + curriculumSubjectId
                                )
                        );


        subject.setActive(false);

        return curriculumMapper.toSubjectResponse(
                curriculumSubjectRepository.save(
                        subject
                )
        );
    }


    // ============================================================
    // DELETE SUBJECT
    // ============================================================

    @Override
    public void deleteSubject(
            Long curriculumSubjectId
    ) {

        CurriculumSubject subject =
                curriculumSubjectRepository
                        .findById(
                                curriculumSubjectId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum subject not found: "
                                                + curriculumSubjectId
                                )
                        );


        curriculumSubjectRepository.delete(
                subject
        );
    }


    // ============================================================
    // VALIDATION
    // ============================================================

    private void validateCreateRequest(
            CurriculumRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Curriculum request is required."
            );
        }

        if (
                request.organizationId() == null ||
                        request.organizationId() <= 0
        ) {
            throw new IllegalArgumentException(
                    "Organization is required."
            );
        }

        if (
                request.academicSessionId() == null ||
                        request.academicSessionId() <= 0
        ) {
            throw new IllegalArgumentException(
                    "Academic session is required."
            );
        }

        if (
                request.classRoomId() == null ||
                        request.classRoomId() <= 0
        ) {
            throw new IllegalArgumentException(
                    "Class is required."
            );
        }

        if (
                request.name() == null ||
                        request.name().isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Curriculum name is required."
            );
        }

        if (
                request.code() == null ||
                        request.code().isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Curriculum code is required."
            );
        }
    }


    private void validateOrganization(
            Long actualOrganizationId,
            Long requestedOrganizationId
    ) {

        if (
                actualOrganizationId == null ||
                        !actualOrganizationId.equals(
                                requestedOrganizationId
                        )
        ) {

            throw new IllegalArgumentException(
                    "Invalid organization."
            );
        }
    }
}