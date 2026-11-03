package com.smartexam.studentenrollment.service.impl;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.classroom.repository.ClassRoomRepository;
import com.smartexam.common.exception.BusinessException;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.section.entity.Section;
import com.smartexam.student.entity.Student;
import com.smartexam.section.repository.SectionRepository;
import com.smartexam.student.repository.StudentRepository;
import com.smartexam.studentenrollment.dto.StudentEnrollmentRequest;
import com.smartexam.studentenrollment.dto.StudentEnrollmentResponse;
import com.smartexam.studentenrollment.dto.StudentEnrollmentSearchRequest;
import com.smartexam.studentenrollment.dto.StudentEnrollmentUpdateRequest;
import com.smartexam.studentenrollment.entity.StudentEnrollment;
import com.smartexam.studentenrollment.mapper.StudentEnrollmentMapper;
import com.smartexam.studentenrollment.repository.StudentEnrollmentRepository;
import com.smartexam.studentenrollment.service.StudentEnrollmentService;
import com.smartexam.studentenrollment.specification.StudentEnrollmentSpecification;
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
public class StudentEnrollmentServiceImpl
        implements StudentEnrollmentService {

    private final StudentEnrollmentRepository repository;
    private final StudentEnrollmentMapper mapper;

    private final StudentRepository studentRepository;
    private final AcademicSessionRepository academicSessionRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SectionRepository sectionRepository;

    @Override
    public StudentEnrollmentResponse create(StudentEnrollmentRequest request) {

        log.info("Creating Student Enrollment : StudentId={}", request.getStudentId());

        // Duplicate Student Enrollment Validation
        if (repository.existsByStudentIdAndAcademicSessionId(
                request.getStudentId(),
                request.getAcademicSessionId())) {

            throw new DuplicateResourceException(
                    "Student Enrollment",
                    "Student is already enrolled in the selected Academic Session.");
        }

        // Duplicate Roll Number Validation
        if (repository.existsByRollNoAndClassRoomIdAndSectionIdAndAcademicSessionId(
                request.getRollNo(),
                request.getClassRoomId(),
                request.getSectionId(),
                request.getAcademicSessionId())) {

            throw new DuplicateResourceException(
                    "Roll Number",
                    "Roll Number already exists.");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student",
                                request.getStudentId()));

        AcademicSession session = academicSessionRepository
                .findById(request.getAcademicSessionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Academic Session",
                                request.getAcademicSessionId()));

        ClassRoom classRoom = classRoomRepository
                .findById(request.getClassRoomId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Class Room",
                                request.getClassRoomId()));

        Section section = sectionRepository
                .findById(request.getSectionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section",
                                request.getSectionId()));

        // Organization Validation
        if (!student.getOrganization().getId()
                .equals(session.getOrganization().getId())) {

            throw new BusinessException(
                    "Student and Academic Session belong to different organizations.");
        }

        if (!student.getOrganization().getId()
                .equals(classRoom.getOrganization().getId())) {

            throw new BusinessException(
                    "Student and Class Room belong to different organizations.");
        }

        if (!student.getOrganization().getId()
                .equals(section.getOrganization().getId())) {

            throw new BusinessException(
                    "Student and Section belong to different organizations.");
        }

        // Section Validation
        if (!section.getClassRoom().getId()
                .equals(classRoom.getId())) {

            throw new BusinessException(
                    "Section does not belong to the selected Class Room.");
        }

        StudentEnrollment enrollment = mapper.toEntity(request);

        enrollment.setStudent(student);
        enrollment.setAcademicSession(session);
        enrollment.setClassRoom(classRoom);
        enrollment.setSection(section);
        enrollment.setActive(true);

        StudentEnrollment saved = repository.save(enrollment);

        log.info("Student Enrollment Created : {}", saved.getId());

        return mapper.toResponse(saved);
    }

    @Override
    public StudentEnrollmentResponse update(
            Long id,
            StudentEnrollmentUpdateRequest request) {

        log.info("Updating Student Enrollment : {}", id);

        StudentEnrollment enrollment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student Enrollment",
                                id));

        Student student = enrollment.getStudent();
        AcademicSession session = enrollment.getAcademicSession();
        ClassRoom classRoom = enrollment.getClassRoom();
        Section section = enrollment.getSection();

        // Update Student
        if (request.getStudentId() != null) {

            student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Student",
                                    request.getStudentId()));
        }

        // Update Academic Session
        if (request.getAcademicSessionId() != null) {

            session = academicSessionRepository.findById(
                            request.getAcademicSessionId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Academic Session",
                                    request.getAcademicSessionId()));
        }

        // Update Class Room
        if (request.getClassRoomId() != null) {

            classRoom = classRoomRepository.findById(
                            request.getClassRoomId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Class Room",
                                    request.getClassRoomId()));
        }

        // Update Section
        if (request.getSectionId() != null) {

            section = sectionRepository.findById(
                            request.getSectionId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Section",
                                    request.getSectionId()));
        }

        // Duplicate Student Enrollment Validation
        if (repository.existsByStudentIdAndAcademicSessionIdAndIdNot(
                student.getId(),
                session.getId(),
                id)) {

            throw new DuplicateResourceException(
                    "Student Enrollment",
                    "Student is already enrolled in the selected Academic Session.");
        }

        // Roll Number Validation
        String rollNo = request.getRollNo() != null
                ? request.getRollNo()
                : enrollment.getRollNo();

        if (repository.existsByRollNoAndClassRoomIdAndSectionIdAndAcademicSessionIdAndIdNot(
                rollNo,
                classRoom.getId(),
                section.getId(),
                session.getId(),
                id)) {

            throw new DuplicateResourceException(
                    "Roll Number",
                    "Roll Number already exists.");
        }

        // Organization Validation
        if (!student.getOrganization().getId()
                .equals(session.getOrganization().getId())) {

            throw new BusinessException(
                    "Student and Academic Session belong to different organizations.");
        }

        if (!student.getOrganization().getId()
                .equals(classRoom.getOrganization().getId())) {

            throw new BusinessException(
                    "Student and Class Room belong to different organizations.");
        }

        if (!student.getOrganization().getId()
                .equals(section.getOrganization().getId())) {

            throw new BusinessException(
                    "Student and Section belong to different organizations.");
        }

        // Section Validation
        if (!section.getClassRoom().getId()
                .equals(classRoom.getId())) {

            throw new BusinessException(
                    "Section does not belong to the selected Class Room.");
        }

        // Partial Update
        mapper.updateEntity(request, enrollment);

        enrollment.setStudent(student);
        enrollment.setAcademicSession(session);
        enrollment.setClassRoom(classRoom);
        enrollment.setSection(section);

        StudentEnrollment updated = repository.save(enrollment);

        log.info("Student Enrollment Updated : {}", updated.getId());

        return mapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentEnrollmentResponse getById(Long id) {

        log.info("Fetching Student Enrollment : {}", id);

        StudentEnrollment enrollment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student Enrollment",
                                id));

        return mapper.toResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentEnrollmentResponse> getAll() {

        log.info("Fetching all Student Enrollments");

        return mapper.toResponseList(
                repository.findAll());
    }
    @Override
    @Transactional(readOnly = true)
    public Page<StudentEnrollmentResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction) {

        log.info("Fetching Student Enrollments with pagination");

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentEnrollmentResponse> search(
            StudentEnrollmentSearchRequest request) {

        log.info("Searching Student Enrollments");

        Specification<StudentEnrollment> specification =
                StudentEnrollmentSpecification.hasKeyword(request.getKeyword())
                        .and(StudentEnrollmentSpecification.hasStudent(request.getStudentId()))
                        .and(StudentEnrollmentSpecification.hasAcademicSession(request.getAcademicSessionId()))
                        .and(StudentEnrollmentSpecification.hasClassRoom(request.getClassRoomId()))
                        .and(StudentEnrollmentSpecification.hasSection(request.getSectionId()))
                        .and(StudentEnrollmentSpecification.hasEnrollmentStatus(request.getEnrollmentStatus()))
                        .and(StudentEnrollmentSpecification.isActive(request.getActive()));

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
    public StudentEnrollmentResponse activate(Long id) {

        log.info("Activating Student Enrollment : {}", id);

        StudentEnrollment enrollment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student Enrollment",
                                id));

        enrollment.setActive(true);

        StudentEnrollment updated = repository.save(enrollment);

        return mapper.toResponse(updated);
    }

    @Override
    public StudentEnrollmentResponse deactivate(Long id) {

        log.info("Deactivating Student Enrollment : {}", id);

        StudentEnrollment enrollment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student Enrollment",
                                id));

        enrollment.setActive(false);

        StudentEnrollment updated = repository.save(enrollment);

        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {

        log.info("Deleting Student Enrollment : {}", id);

        StudentEnrollment enrollment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student Enrollment",
                                id));

        enrollment.setActive(false);

        repository.save(enrollment);
    }
}