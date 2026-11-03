package com.smartexam.teacherSubjectAssignment.service.impl;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.classroom.repository.ClassRoomRepository;
import com.smartexam.common.exception.BusinessException;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.section.entity.Section;
import com.smartexam.section.repository.SectionRepository;
import com.smartexam.subject.entity.Subject;
import com.smartexam.subject.repository.SubjectRepository;
import com.smartexam.teacher.entity.Teacher;
import com.smartexam.teacher.repository.TeacherRepository;
import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentRequest;
import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentResponse;
import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentSearchRequest;
import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentUpdateRequest;
import com.smartexam.teacherSubjectAssignment.entity.TeacherSubjectAssignment;
import com.smartexam.teacherSubjectAssignment.mapper.TeacherSubjectAssignmentMapper;
import com.smartexam.teacherSubjectAssignment.repository.TeacherSubjectAssignmentRepository;
import com.smartexam.teacherSubjectAssignment.service.TeacherSubjectAssignmentService;
import com.smartexam.teacherSubjectAssignment.specification.TeacherSubjectAssignmentSpecification;
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
public class TeacherSubjectAssignmentServiceImpl
        implements TeacherSubjectAssignmentService {

    private final TeacherSubjectAssignmentRepository repository;
    private final TeacherSubjectAssignmentMapper mapper;

    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SectionRepository sectionRepository;
    private final AcademicSessionRepository academicSessionRepository;

    @Override
    public TeacherSubjectAssignmentResponse create(
            TeacherSubjectAssignmentRequest request) {

        log.info("Assigning Teacher {} to Subject {}", request.getTeacherId(), request.getSubjectId());

        if (repository.existsByTeacherIdAndSubjectIdAndClassRoomIdAndSectionIdAndAcademicSessionId(request.getTeacherId(), request.getSubjectId(), request.getClassRoomId(), request.getSectionId(), request.getAcademicSessionId())) {

            throw new DuplicateResourceException("Teacher Subject Assignment", "already exists");
        }

        Teacher teacher = teacherRepository.findById(request.getTeacherId()).orElseThrow(() -> new ResourceNotFoundException("Teacher", request.getTeacherId()));

        Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> new ResourceNotFoundException("Subject", request.getSubjectId()));

        ClassRoom classRoom = classRoomRepository.findById(request.getClassRoomId()).orElseThrow(() -> new ResourceNotFoundException("ClassRoom", request.getClassRoomId()));

        Section section = sectionRepository.findById(request.getSectionId()).orElseThrow(() -> new ResourceNotFoundException("Section", request.getSectionId()));

        AcademicSession session = academicSessionRepository.findById(request.getAcademicSessionId()).orElseThrow(() -> new ResourceNotFoundException("Academic Session", request.getAcademicSessionId()));

        if (!teacher.getOrganization().getId()
                .equals(subject.getOrganization().getId())) {

            throw new BusinessException(
                    "Teacher and Subject belong to different organizations.");
        }

        if (!teacher.getOrganization().getId()
                .equals(classRoom.getOrganization().getId())) {

            throw new BusinessException(
                    "Teacher and ClassRoom belong to different organizations.");
        }

        if (!teacher.getOrganization().getId()
                .equals(section.getOrganization().getId())) {

            throw new BusinessException(
                    "Teacher and Section belong to different organizations.");
        }

        if (!teacher.getOrganization().getId()
                .equals(session.getOrganization().getId())) {

            throw new BusinessException(
                    "Teacher and Academic Session belong to different organizations.");
        }
        if (!section.getClassRoom().getId()
                .equals(classRoom.getId())) {

            throw new BusinessException(
                    "Section does not belong to selected ClassRoom.");
        }
        if (!subject.getAcademicSession().getId()
                .equals(session.getId())) {

            throw new BusinessException(
                    "Subject does not belong to selected Academic Session.");
        }
        if (Boolean.TRUE.equals(request.getClassTeacher())) {

            boolean alreadyExists =
                    repository.existsByClassRoomIdAndSectionIdAndAcademicSessionIdAndClassTeacherTrue(
                            request.getClassRoomId(),
                            request.getSectionId(),
                            request.getAcademicSessionId());

            if (alreadyExists) {

                throw new BusinessException(
                        "Class Teacher already assigned.");
            }
        }
        TeacherSubjectAssignment assignment =
                mapper.toEntity(request);

        assignment.setTeacher(teacher);
        assignment.setSubject(subject);
        assignment.setClassRoom(classRoom);
        assignment.setSection(section);
        assignment.setAcademicSession(session);
        assignment.setActive(true);

        TeacherSubjectAssignment saved =
                repository.save(assignment);

        log.info("Teacher Assignment Created : {}", saved.getId());

        return mapper.toResponse(saved);
    }

    @Override
    public TeacherSubjectAssignmentResponse update(
            Long id,
            TeacherSubjectAssignmentUpdateRequest request) {

        log.info("Updating Teacher Subject Assignment : {}", id);

        TeacherSubjectAssignment assignment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher Subject Assignment",
                                id));

        Teacher teacher = assignment.getTeacher();
        Subject subject = assignment.getSubject();
        ClassRoom classRoom = assignment.getClassRoom();
        Section section = assignment.getSection();
        AcademicSession session = assignment.getAcademicSession();

        // Update Teacher
        if (request.getTeacherId() != null) {

            teacher = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Teacher",
                                    request.getTeacherId()));
        }

        // Update Subject
        if (request.getSubjectId() != null) {

            subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Subject",
                                    request.getSubjectId()));
        }

        // Update ClassRoom
        if (request.getClassRoomId() != null) {

            classRoom = classRoomRepository.findById(request.getClassRoomId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "ClassRoom",
                                    request.getClassRoomId()));
        }

        // Update Section
        if (request.getSectionId() != null) {

            section = sectionRepository.findById(request.getSectionId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Section",
                                    request.getSectionId()));
        }

        // Update Academic Session
        if (request.getAcademicSessionId() != null) {

            session = academicSessionRepository.findById(request.getAcademicSessionId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Academic Session",
                                    request.getAcademicSessionId()));
        }

        // Duplicate Validation
        boolean duplicate = repository
                .existsByTeacherIdAndSubjectIdAndClassRoomIdAndSectionIdAndAcademicSessionIdAndIdNot(
                        teacher.getId(),
                        subject.getId(),
                        classRoom.getId(),
                        section.getId(),
                        session.getId(),
                        id);

        if (duplicate) {

            throw new DuplicateResourceException(
                    "Teacher Subject Assignment",
                    "already exists");
        }

        // Organization Validation
        if (!teacher.getOrganization().getId().equals(subject.getOrganization().getId())) {
            throw new BusinessException("Teacher and Subject belong to different organizations.");
        }

        if (!teacher.getOrganization().getId().equals(classRoom.getOrganization().getId())) {
            throw new BusinessException("Teacher and ClassRoom belong to different organizations.");
        }

        if (!teacher.getOrganization().getId().equals(section.getOrganization().getId())) {
            throw new BusinessException("Teacher and Section belong to different organizations.");
        }

        if (!teacher.getOrganization().getId().equals(session.getOrganization().getId())) {
            throw new BusinessException("Teacher and Academic Session belong to different organizations.");
        }

        // Section Validation
        if (!section.getClassRoom().getId().equals(classRoom.getId())) {

            throw new BusinessException(
                    "Section does not belong to selected ClassRoom.");
        }

        // Subject Validation
        if (!subject.getAcademicSession().getId().equals(session.getId())) {

            throw new BusinessException(
                    "Subject does not belong to selected Academic Session.");
        }

        // Class Teacher Validation
        boolean classTeacher = request.getClassTeacher() != null
                ? request.getClassTeacher()
                : assignment.getClassTeacher();

        if (classTeacher) {

            boolean exists = repository
                    .existsByClassRoomIdAndSectionIdAndAcademicSessionIdAndClassTeacherTrueAndIdNot(
                            classRoom.getId(),
                            section.getId(),
                            session.getId(),
                            id);

            if (exists) {

                throw new BusinessException(
                        "Class Teacher already assigned.");
            }
        }

        // Update Entity
        mapper.updateEntity(request, assignment);

        assignment.setTeacher(teacher);
        assignment.setSubject(subject);
        assignment.setClassRoom(classRoom);
        assignment.setSection(section);
        assignment.setAcademicSession(session);

        TeacherSubjectAssignment updated = repository.save(assignment);

        log.info("Teacher Subject Assignment Updated : {}", updated.getId());

        return mapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherSubjectAssignmentResponse getById(Long id) {

        log.info("Fetching Teacher Subject Assignment : {}", id);

        TeacherSubjectAssignment assignment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher Subject Assignment",
                                id));

        return mapper.toResponse(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherSubjectAssignmentResponse> getAll() {

        log.info("Fetching all Teacher Subject Assignments");

        return mapper.toResponseList(
                repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeacherSubjectAssignmentResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction) {

        log.info("Fetching Teacher Subject Assignments with pagination");

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeacherSubjectAssignmentResponse> search(
            TeacherSubjectAssignmentSearchRequest request) {

        log.info("Searching Teacher Subject Assignments");

        Specification<TeacherSubjectAssignment> specification =
                TeacherSubjectAssignmentSpecification.hasKeyword(request.getKeyword())
                        .and(TeacherSubjectAssignmentSpecification.hasTeacher(request.getTeacherId()))
                        .and(TeacherSubjectAssignmentSpecification.hasSubject(request.getSubjectId()))
                        .and(TeacherSubjectAssignmentSpecification.hasClassRoom(request.getClassRoomId()))
                        .and(TeacherSubjectAssignmentSpecification.hasSection(request.getSectionId()))
                        .and(TeacherSubjectAssignmentSpecification.hasAcademicSession(request.getAcademicSessionId()))
                        .and(TeacherSubjectAssignmentSpecification.isClassTeacher(request.getClassTeacher()))
                        .and(TeacherSubjectAssignmentSpecification.isActive(request.getActive()));

        Sort sort = request.getDirection().equalsIgnoreCase("DESC")
                ? Sort.by(request.getSortBy()).descending()
                : Sort.by(request.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort);

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public TeacherSubjectAssignmentResponse activate(Long id) {

        log.info("Activating Teacher Subject Assignment : {}", id);

        TeacherSubjectAssignment assignment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher Subject Assignment",
                                id));

        assignment.setActive(true);

        return mapper.toResponse(
                repository.save(assignment));
    }

    @Override
    public TeacherSubjectAssignmentResponse deactivate(Long id) {

        log.info("Deactivating Teacher Subject Assignment : {}", id);

        TeacherSubjectAssignment assignment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher Subject Assignment",
                                id));

        assignment.setActive(false);

        return mapper.toResponse(
                repository.save(assignment));
    }

    @Override
    public void delete(Long id) {

        log.info("Deleting Teacher Subject Assignment : {}", id);

        TeacherSubjectAssignment assignment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher Subject Assignment",
                                id));

        assignment.setActive(false);

        repository.save(assignment);
    }

}
