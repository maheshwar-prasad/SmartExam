package com.smartexam.syllabus.service;

import com.smartexam.classsubject.entity.ClassSubject;
import com.smartexam.classsubject.repository.ClassSubjectRepository;
import com.smartexam.curriculum.entity.Curriculum;
import com.smartexam.curriculum.repository.CurriculumRepository;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.syllabus.dto.SyllabusRequest;
import com.smartexam.syllabus.dto.SyllabusResponse;
import com.smartexam.syllabus.dto.SyllabusUpdateRequest;
import com.smartexam.syllabus.entity.Syllabus;
import com.smartexam.syllabus.mapper.SyllabusMapper;
import com.smartexam.syllabus.repository.SyllabusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusServiceImpl
        implements SyllabusService {


    private final SyllabusRepository syllabusRepository;

    private final SyllabusMapper syllabusMapper;

    private final OrganizationRepository organizationRepository;

    private final CurriculumRepository curriculumRepository;

    private final ClassSubjectRepository classSubjectRepository;


    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public SyllabusResponse create(
            SyllabusRequest request
    ) {

        Organization organization =
                organizationRepository
                        .findById(request.organizationId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Organization not found: "
                                                + request.organizationId()
                                )
                        );


        Curriculum curriculum =
                curriculumRepository
                        .findById(request.curriculumId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Curriculum not found: "
                                                + request.curriculumId()
                                )
                        );


        ClassSubject classSubject =
                classSubjectRepository
                        .findById(request.classSubjectId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Class subject not found: "
                                                + request.classSubjectId()
                                )
                        );


        validateClassSubject(
                curriculum,
                classSubject
        );


        Syllabus syllabus =
                new Syllabus();

        syllabus.setOrganization(
                organization
        );

        syllabus.setCurriculum(
                curriculum
        );

        syllabus.setClassSubject(
                classSubject
        );

        syllabus.setTitle(
                request.title().trim()
        );

        syllabus.setChapterName(
                request.chapterName().trim()
        );

        syllabus.setTopicName(
                request.topicName()
        );

        syllabus.setDescription(
                request.description()
        );

        syllabus.setSequenceNo(
                request.sequenceNo()
        );

        syllabus.setEstimatedHours(
                request.estimatedHours()
        );

        syllabus.setActive(
                request.active() == null
                        ? true
                        : request.active()
        );


        Syllabus saved =
                syllabusRepository.save(
                        syllabus
                );


        return syllabusMapper.toResponse(
                saved
        );
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public SyllabusResponse update(
            Long id,
            SyllabusUpdateRequest request
    ) {

        Syllabus syllabus =
                syllabusRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Syllabus not found: " + id
                                )
                        );


        Organization organization =
                organizationRepository
                        .findById(request.organizationId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Organization not found: "
                                                + request.organizationId()
                                )
                        );


        Curriculum curriculum =
                curriculumRepository
                        .findById(request.curriculumId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Curriculum not found: "
                                                + request.curriculumId()
                                )
                        );


        ClassSubject classSubject =
                classSubjectRepository
                        .findById(request.classSubjectId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Class subject not found: "
                                                + request.classSubjectId()
                                )
                        );


        validateClassSubject(
                curriculum,
                classSubject
        );


        syllabus.setOrganization(
                organization
        );

        syllabus.setCurriculum(
                curriculum
        );

        syllabus.setClassSubject(
                classSubject
        );

        syllabus.setTitle(
                request.title().trim()
        );

        syllabus.setChapterName(
                request.chapterName().trim()
        );

        syllabus.setTopicName(
                request.topicName()
        );

        syllabus.setDescription(
                request.description()
        );

        syllabus.setSequenceNo(
                request.sequenceNo()
        );

        syllabus.setEstimatedHours(
                request.estimatedHours()
        );

        if (request.active() != null) {

            syllabus.setActive(
                    request.active()
            );

        }


        Syllabus saved =
                syllabusRepository.save(
                        syllabus
                );


        return syllabusMapper.toResponse(
                saved
        );
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public SyllabusResponse getById(
            Long id
    ) {

        Syllabus syllabus =
                syllabusRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Syllabus not found: " + id
                                )
                        );


        return syllabusMapper.toResponse(
                syllabus
        );
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SyllabusResponse> getAll() {

        return syllabusRepository
                .findAll()
                .stream()
                .map(syllabusMapper::toResponse)
                .toList();
    }


    // =========================================================
    // ORGANIZATION
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SyllabusResponse> getByOrganization(
            Long organizationId
    ) {

        return syllabusRepository
                .findByOrganization_Id(
                        organizationId
                )
                .stream()
                .map(syllabusMapper::toResponse)
                .toList();
    }


    // =========================================================
    // CURRICULUM
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SyllabusResponse> getByCurriculum(
            Long curriculumId
    ) {

        return syllabusRepository
                .findByCurriculum_IdOrderBySequenceNoAsc(
                        curriculumId
                )
                .stream()
                .map(syllabusMapper::toResponse)
                .toList();
    }


    // =========================================================
    // CLASS SUBJECT
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SyllabusResponse> getByClassSubject(
            Long classSubjectId
    ) {

        return syllabusRepository
                .findByClassSubject_Id(
                        classSubjectId
                )
                .stream()
                .map(syllabusMapper::toResponse)
                .toList();
    }


    // =========================================================
    // ACTIVE BY CURRICULUM
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SyllabusResponse> getActiveByCurriculum(
            Long curriculumId
    ) {

        return syllabusRepository
                .findByCurriculum_IdAndActiveTrue(
                        curriculumId
                )
                .stream()
                .map(syllabusMapper::toResponse)
                .toList();
    }


    // =========================================================
    // CURRICULUM + CLASS SUBJECT
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SyllabusResponse>
    getByCurriculumAndClassSubject(
            Long curriculumId,
            Long classSubjectId
    ) {

        return syllabusRepository
                .findByCurriculum_IdAndClassSubject_IdOrderBySequenceNoAsc(
                        curriculumId,
                        classSubjectId
                )
                .stream()
                .map(syllabusMapper::toResponse)
                .toList();
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @Override
    public SyllabusResponse activate(
            Long id
    ) {

        Syllabus syllabus =
                syllabusRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Syllabus not found: " + id
                                )
                        );


        syllabus.setActive(true);


        return syllabusMapper.toResponse(
                syllabusRepository.save(syllabus)
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Override
    public SyllabusResponse deactivate(
            Long id
    ) {

        Syllabus syllabus =
                syllabusRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Syllabus not found: " + id
                                )
                        );


        syllabus.setActive(false);


        return syllabusMapper.toResponse(
                syllabusRepository.save(syllabus)
        );
    }


    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public void delete(
            Long id
    ) {

        Syllabus syllabus =
                syllabusRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Syllabus not found: " + id
                                )
                        );


        syllabusRepository.delete(
                syllabus
        );
    }


    // =========================================================
    // VALIDATION
    // =========================================================

    private void validateClassSubject(
            Curriculum curriculum,
            ClassSubject classSubject
    ) {

        if (
                classSubject.getClassRoom() == null ||
                        curriculum.getClassRoom() == null ||
                        !classSubject.getClassRoom()
                                .getId()
                                .equals(
                                        curriculum.getClassRoom().getId()
                                )
        ) {

            throw new IllegalArgumentException(
                    "Class subject does not belong to "
                            + "the curriculum class."
            );
        }
    }
}