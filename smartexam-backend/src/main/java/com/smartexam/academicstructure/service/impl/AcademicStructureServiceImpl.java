package com.smartexam.academicstructure.service;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.repository.AcademicSessionRepository;

import com.smartexam.academicstructure.dto.AcademicStructureRequest;
import com.smartexam.academicstructure.dto.AcademicStructureResponse;
import com.smartexam.academicstructure.dto.AcademicStructureUpdateRequest;
import com.smartexam.academicstructure.entity.AcademicStructure;
import com.smartexam.academicstructure.mapper.AcademicStructureMapper;
import com.smartexam.academicstructure.repository.AcademicStructureRepository;

import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.classroom.repository.ClassRoomRepository;

import com.smartexam.common.exception.BusinessException;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;

import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;

import com.smartexam.section.entity.Section;
import com.smartexam.section.repository.SectionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AcademicStructureServiceImpl
        implements AcademicStructureService {

    private final AcademicStructureRepository repository;

    private final AcademicStructureMapper mapper;

    private final OrganizationRepository organizationRepository;

    private final AcademicSessionRepository academicSessionRepository;

    private final ClassRoomRepository classRoomRepository;

    private final SectionRepository sectionRepository;


    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public AcademicStructureResponse create(
            AcademicStructureRequest request) {

        log.info(
                "Creating Academic Structure : {}",
                request.code()
        );

        validateDuplicateCode(
                request.organizationId(),
                request.academicSessionId(),
                request.code()
        );

        Organization organization =
                organizationRepository.findById(
                        request.organizationId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Organization",
                                request.organizationId()
                        )
                );


        AcademicSession academicSession =
                academicSessionRepository.findById(
                        request.academicSessionId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Academic Session",
                                request.academicSessionId()
                        )
                );


        ClassRoom classRoom =
                classRoomRepository.findById(
                        request.classRoomId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Class Room",
                                request.classRoomId()
                        )
                );


        Section section =
                sectionRepository.findById(
                        request.sectionId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section",
                                request.sectionId()
                        )
                );


        validateRelationships(
                organization,
                academicSession,
                classRoom,
                section
        );


        AcademicStructure entity =
                AcademicStructure.builder()
                        .organization(organization)
                        .academicSession(academicSession)
                        .classRoom(classRoom)
                        .section(section)
                        .name(request.name())
                        .code(request.code())
                        .description(request.description())
                        .active(
                                request.active() != null
                                        ? request.active()
                                        : true
                        )
                        .build();


        AcademicStructure saved =
                repository.save(entity);

        log.info(
                "Academic Structure created : {}",
                saved.getId()
        );

        return mapper.toResponse(saved);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public AcademicStructureResponse update(
            Long id,
            AcademicStructureUpdateRequest request) {

        log.info(
                "Updating Academic Structure : {}",
                id
        );

        AcademicStructure entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Academic Structure",
                                        id
                                )
                        );


        if (
                repository
                        .existsByOrganization_IdAndAcademicSession_IdAndCodeAndIdNot(
                                request.organizationId(),
                                request.academicSessionId(),
                                request.code(),
                                id
                        )
        ) {

            throw new DuplicateResourceException(
                    "Academic Structure",
                    "Academic structure code already exists."
            );
        }


        Organization organization =
                organizationRepository.findById(
                        request.organizationId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Organization",
                                request.organizationId()
                        )
                );


        AcademicSession academicSession =
                academicSessionRepository.findById(
                        request.academicSessionId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Academic Session",
                                request.academicSessionId()
                        )
                );


        ClassRoom classRoom =
                classRoomRepository.findById(
                        request.classRoomId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Class Room",
                                request.classRoomId()
                        )
                );


        Section section =
                sectionRepository.findById(
                        request.sectionId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section",
                                request.sectionId()
                        )
                );


        validateRelationships(
                organization,
                academicSession,
                classRoom,
                section
        );


        entity.setOrganization(organization);

        entity.setAcademicSession(
                academicSession
        );

        entity.setClassRoom(
                classRoom
        );

        entity.setSection(
                section
        );

        entity.setName(
                request.name()
        );

        entity.setCode(
                request.code()
        );

        entity.setDescription(
                request.description()
        );

        if (request.active() != null) {

            entity.setActive(
                    request.active()
            );
        }


        AcademicStructure updated =
                repository.save(entity);

        return mapper.toResponse(updated);
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public AcademicStructureResponse getById(
            Long id) {

        AcademicStructure entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Academic Structure",
                                        id
                                )
                        );

        return mapper.toResponse(entity);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<AcademicStructureResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // BY ORGANIZATION
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<AcademicStructureResponse>
    getByOrganization(Long organizationId) {

        return repository
                .findByOrganization_Id(organizationId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // BY ACADEMIC SESSION
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<AcademicStructureResponse>
    getByAcademicSession(
            Long academicSessionId) {

        return repository
                .findByAcademicSession_Id(
                        academicSessionId
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // BY CLASS ROOM
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<AcademicStructureResponse>
    getByClassRoom(Long classRoomId) {

        return repository
                .findByClassRoom_Id(classRoomId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // BY SECTION
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<AcademicStructureResponse>
    getBySection(Long sectionId) {

        return repository
                .findBySection_Id(sectionId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // BY SESSION + CLASS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<AcademicStructureResponse>
    getBySessionAndClass(
            Long academicSessionId,
            Long classRoomId) {

        return repository
                .findByAcademicSession_IdAndClassRoom_Id(
                        academicSessionId,
                        classRoomId
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // ACTIVE BY ORGANIZATION
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<AcademicStructureResponse>
    getActiveByOrganization(
            Long organizationId) {

        return repository
                .findByOrganization_IdAndActiveTrue(
                        organizationId
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @Override
    public AcademicStructureResponse activate(
            Long id) {

        AcademicStructure entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Academic Structure",
                                        id
                                )
                        );

        entity.setActive(true);

        return mapper.toResponse(
                repository.save(entity)
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Override
    public AcademicStructureResponse deactivate(
            Long id) {

        AcademicStructure entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Academic Structure",
                                        id
                                )
                        );

        entity.setActive(false);

        return mapper.toResponse(
                repository.save(entity)
        );
    }


    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public void delete(Long id) {

        AcademicStructure entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Academic Structure",
                                        id
                                )
                        );

        repository.delete(entity);
    }


    // =========================================================
    // VALIDATION
    // =========================================================

    private void validateDuplicateCode(
            Long organizationId,
            Long academicSessionId,
            String code) {

        if (
                repository
                        .existsByOrganization_IdAndAcademicSession_IdAndCode(
                                organizationId,
                                academicSessionId,
                                code
                        )
        ) {

            throw new DuplicateResourceException(
                    "Academic Structure",
                    "Academic structure code already exists."
            );
        }
    }


    private void validateRelationships(
            Organization organization,
            AcademicSession academicSession,
            ClassRoom classRoom,
            Section section) {


        // -----------------------------------------------------
        // ORGANIZATION VALIDATION
        // -----------------------------------------------------

        if (
                academicSession.getOrganization() == null ||
                        !organization.getId()
                                .equals(
                                        academicSession
                                                .getOrganization()
                                                .getId()
                                )
        ) {

            throw new BusinessException(
                    "Academic Session does not belong to the selected organization."
            );
        }


        if (
                classRoom.getOrganization() == null ||
                        !organization.getId()
                                .equals(
                                        classRoom
                                                .getOrganization()
                                                .getId()
                                )
        ) {

            throw new BusinessException(
                    "Class Room does not belong to the selected organization."
            );
        }


        if (
                section.getOrganization() == null ||
                        !organization.getId()
                                .equals(
                                        section
                                                .getOrganization()
                                                .getId()
                                )
        ) {

            throw new BusinessException(
                    "Section does not belong to the selected organization."
            );
        }


        // -----------------------------------------------------
        // SECTION → CLASS VALIDATION
        // -----------------------------------------------------

        if (
                section.getClassRoom() == null ||
                        !section
                                .getClassRoom()
                                .getId()
                                .equals(
                                        classRoom.getId()
                                )
        ) {

            throw new BusinessException(
                    "Section does not belong to the selected Class Room."
            );
        }
    }
}