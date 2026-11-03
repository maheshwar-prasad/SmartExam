package com.smartexam.student.service.impl;

import com.smartexam.audit.service.AuditService;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.classroom.repository.ClassRoomRepository;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.role.entity.Role;
import com.smartexam.role.repository.RoleRepository;
import com.smartexam.section.entity.Section;
import com.smartexam.section.repository.SectionRepository;
import com.smartexam.student.dto.StudentRequest;
import com.smartexam.student.dto.StudentResponse;
import com.smartexam.student.dto.StudentSearchRequest;
import com.smartexam.student.dto.StudentUpdateRequest;
import com.smartexam.student.entity.Student;
import com.smartexam.student.mapper.StudentMapper;
import com.smartexam.student.repository.StudentRepository;
import com.smartexam.student.service.StudentService;
import com.smartexam.student.specification.StudentSpecification;
import com.smartexam.user.entity.User;
import com.smartexam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    private final OrganizationRepository organizationRepository;

    private final UserRepository userRepository;

    private final ClassRoomRepository classRoomRepository;

    private final SectionRepository sectionRepository;

    private final RoleRepository roleRepository;
    private final AuditService auditService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public StudentResponse create(StudentRequest request) {

        log.info(
                "Creating student: {} {}",
                request.getFirstName(),
                request.getLastName()
        );
        // =====================================================
        // ORGANIZATION
        // =====================================================

        Organization organization =
                organizationRepository.findById(
                        request.getOrganizationId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Organization",
                                request.getOrganizationId()
                        )
                );
        // =====================================================
        // CLASS
        // =====================================================

        ClassRoom classRoom =
                classRoomRepository.findById(
                        request.getClassId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "ClassRoom",
                                request.getClassId()
                        )
                );


        // =====================================================
        // SECTION
        // =====================================================

        Section section =
                sectionRepository.findById(
                        request.getSectionId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section",
                                request.getSectionId()
                        )
                );


        // =====================================================
        // GENERATE STUDENT IDENTIFIERS
        // =====================================================

        String studentCode =
                generateStudentCode();

        String admissionNo =
                generateAdmissionNo();

        String rollNo =
                generateRollNo(
                        request.getClassId(),
                        request.getSectionId()
                );


        // =====================================================
        // CREATE USER
        // =====================================================

        User user =
                createStudentUser(request);


        // =====================================================
        // CREATE STUDENT
        // =====================================================

        Student student =
                studentMapper.toEntity(request);

        student.setStudentCode(studentCode);
        student.setAdmissionNo(admissionNo);
        student.setRollNo(rollNo);
        student.setAdmissionDate(LocalDate.now());
        student.setOrganization(organization);
        student.setUser(user);
        student.setClassRoom(classRoom);
        student.setSection(section);
        student.setActive(true);

        // =====================================================
        // SAVE
        // =====================================================
        Student saved =
                studentRepository.save(student);
        log.info(
                "Student created successfully. id={}, code={}, admissionNo={}, rollNo={}",
                saved.getId(),
                saved.getStudentCode(),
                saved.getAdmissionNo(),
                saved.getRollNo()
        );


        return studentMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {

        Student student =
                studentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student",
                                        id));

        return studentMapper.toResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {

        return studentMapper.toResponseList(
                studentRepository.findAll());

    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return studentRepository
                .findAll(pageable)
                .map(studentMapper::toResponse);

    }

    @Override
    public StudentResponse update(Long id,
            StudentUpdateRequest request) {

        log.info("Updating Student : {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student",
                                id));

        studentMapper.updateEntity(request, student);

        Student updated = studentRepository.save(student);

        log.info("Student Updated Successfully : {}", updated.getId());

        return studentMapper.toResponse(updated);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> search(StudentSearchRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                request.getDirection().equalsIgnoreCase("DESC")
                        ? Sort.by(request.getSortBy()).descending()
                        : Sort.by(request.getSortBy()).ascending()
        );

        Specification<Student> specification =
                Specification.where(
                                StudentSpecification.hasKeyword(request.getKeyword()))
                        .and(
                                StudentSpecification.hasOrganization(
                                        request.getOrganizationId()))
                        .and(
                                StudentSpecification.hasClass(
                                        request.getClassId()))
                        .and(
                                StudentSpecification.hasSection(
                                        request.getSectionId()))
                        .and(
                                StudentSpecification.isActive(
                                        request.getActive()));

        return studentRepository.findAll(specification, pageable)
                .map(studentMapper::toResponse);

    }

    @Override
    public StudentResponse activate(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student",
                                id));

        student.setActive(true);

        Student updated = studentRepository.save(student);

        return studentMapper.toResponse(updated);

    }

    @Override
    public StudentResponse deactivate(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student",
                                id));

        student.setActive(false);

        Student updated = studentRepository.save(student);

        return studentMapper.toResponse(updated);

    }

    @Override
    public void delete(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student",
                                id));

        student.setActive(false);

        studentRepository.save(student);

        log.info("Student Soft Deleted : {}", id);

    }

    private String generateStudentCode() {

        int year = LocalDate.now().getYear();

        long count = studentRepository.count();

        return String.format(
                "STU-%d-%04d",
                year,
                count + 1
        );
    }

    private String generateAdmissionNo() {

        int year = LocalDate.now().getYear();

        long count = studentRepository.count();

        return String.format(
                "ADM-%d-%04d",
                year,
                count + 1
        );
    }

    private String generateRollNo(
            Long classId,
            Long sectionId) {

        Integer maxRollNo =
                studentRepository.findMaxRollNo(
                        classId,
                        sectionId
                );

        return String.valueOf(maxRollNo + 1);
    }
    private User createStudentUser(StudentRequest request) {

        // Check whether email already belongs to another user
        if (request.getEmail() != null
                && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "User Email",
                    request.getEmail()
            );
        }

//        User user = User.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .mobile(request.getMobile())
//                .active(true)
//                .build();
        Organization organization =
                organizationRepository.findById(
                        request.getOrganizationId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Organization",
                                request.getOrganizationId()
                        ));
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                // Account status
                .active(true)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .organization(organization)
                .password(passwordEncoder.encode(
                        UUID.randomUUID().toString()
                ))
                .build();
                // Password - temporary for development
        // Set STUDENT role
        Role studentRole = roleRepository
                .findByName("STUDENT")
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role",
                                "STUDENT"
                        )
                );
        user.setRole(studentRole);
        return userRepository.save(user);
    }

}