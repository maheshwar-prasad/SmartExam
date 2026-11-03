package com.smartexam.teacher.service.impl;

import com.smartexam.common.exception.BusinessException;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.teacher.dto.*;
import com.smartexam.teacher.entity.Teacher;
import com.smartexam.teacher.mapper.TeacherMapper;
import com.smartexam.teacher.repository.TeacherRepository;
import com.smartexam.teacher.service.FileStorageService;
import com.smartexam.teacher.service.TeacherService;
import com.smartexam.teacher.specification.TeacherSpecification;
import com.smartexam.user.entity.User;
import com.smartexam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import com.smartexam.employee.entity.Employee;
import com.smartexam.employee.enums.EmployeeType;
import com.smartexam.employee.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository repository;
    private final TeacherMapper mapper;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final EmployeeRepository employeeRepository;

//    @Override
//    public TeacherResponse create(TeacherRequest request) {
//        if (request.getEmail() != null &&
//                repository.existsByEmailIgnoreCaseAndOrganizationId(
//                        request.getEmail(),
//                        request.getOrganizationId())) {
//
//            throw new DuplicateResourceException(
//                    "Email",
//                    request.getEmail());
//        }
//
//        if (request.getMobile() != null &&
//                repository.existsByMobileAndOrganizationId(
//                        request.getMobile(),
//                        request.getOrganizationId())) {
//
//            throw new DuplicateResourceException(
//                    "Mobile",
//                    request.getMobile());
//        }
//
//        if (repository.existsByUserId(request.getUserId())) {
//
//            throw new BusinessException(
//                    "User is already assigned to another Teacher.");
//        }
//
//        Organization organization =
//                organizationRepository.findById(
//                                request.getOrganizationId())
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Organization",
//                                        request.getOrganizationId()));
//
//        User user =
//                userRepository.findById(
//                                request.getUserId())
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "User",
//                                        request.getUserId()));
//
//        if (!organization.getId().equals(
//                user.getOrganization().getId())) {
//
//            throw new BusinessException(
//                    "User does not belong to Organization.");
//        }
//        Teacher teacher =
//                mapper.toEntity(request);
//        teacher.setEmployeeCode(
//                "TMP-" + System.currentTimeMillis()
//        );
//
//        teacher.setOrganization(organization);
//        teacher.setUser(user);
//        teacher.setActive(true);
//        Teacher savedTeacher =
//                repository.saveAndFlush(teacher);
//        savedTeacher.setEmployeeCode(String.format("TCH-%05d", savedTeacher.getId()));
//        savedTeacher = repository.save(savedTeacher);
//
//        return mapper.toResponse(savedTeacher);
//    }

    @Override
    public TeacherResponse create(TeacherRequest request) {

        if (request.getEmail() != null &&
                repository.existsByEmailIgnoreCaseAndOrganizationId(
                        request.getEmail(),
                        request.getOrganizationId())) {

            throw new DuplicateResourceException(
                    "Email",
                    request.getEmail()
            );
        }

        if (request.getMobile() != null &&
                repository.existsByMobileAndOrganizationId(
                        request.getMobile(),
                        request.getOrganizationId())) {

            throw new DuplicateResourceException(
                    "Mobile",
                    request.getMobile()
            );
        }

        if (repository.existsByUserId(request.getUserId())) {
            throw new BusinessException(
                    "User is already assigned to another Teacher."
            );
        }

        Organization organization = organizationRepository
                .findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Organization",
                        request.getOrganizationId()
                ));

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User",
                        request.getUserId()
                ));

        if (user.getOrganization() == null ||
                !organization.getId().equals(
                        user.getOrganization().getId())) {

            throw new BusinessException(
                    "User does not belong to Organization."
            );
        }

        Teacher teacher = mapper.toEntity(request);
        teacher.setEmployeeCode("TMP-" + System.currentTimeMillis());
        teacher.setOrganization(organization);
        teacher.setUser(user);
        teacher.setActive(true);

        Teacher savedTeacher = repository.saveAndFlush(teacher);

        String employeeCode =
                String.format("TCH-%05d", savedTeacher.getId());

        savedTeacher.setEmployeeCode(employeeCode);

        Employee employee = new Employee();
        employee.setOrganization(organization);
        employee.setUser(user);
        employee.setEmployeeCode(employeeCode);
        employee.setFirstName(savedTeacher.getFirstName());
        employee.setLastName(savedTeacher.getLastName());
        employee.setEmployeeType(EmployeeType.TEACHER);
        employee.setGender(savedTeacher.getGender());
        employee.setDateOfBirth(savedTeacher.getDateOfBirth());
        employee.setJoiningDate(savedTeacher.getJoiningDate());
        employee.setEmail(savedTeacher.getEmail());
        employee.setMobile(savedTeacher.getMobile());
        employee.setAddress(savedTeacher.getAddress());
        employee.setActive(true);

        Employee savedEmployee = employeeRepository.save(employee);

        savedTeacher.setEmployee(savedEmployee);
        savedTeacher = repository.save(savedTeacher);

        return mapper.toResponse(savedTeacher);
    }

    @Override
    public TeacherResponse update(
            Long id,
            TeacherUpdateRequest request) {

        Teacher teacher =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher",
                                        id));

        Long organizationId =
                request.getOrganizationId() != null
                        ? request.getOrganizationId()
                        : teacher.getOrganization().getId();

        if (request.getEmployeeCode() != null &&
                repository.existsByEmployeeCodeIgnoreCaseAndOrganizationIdAndIdNot(
                        request.getEmployeeCode(),
                        organizationId,
                        id)) {

            throw new DuplicateResourceException(
                    "Employee Code",
                    request.getEmployeeCode());
        }

        if (request.getEmail() != null &&
                repository.existsByEmailIgnoreCaseAndOrganizationIdAndIdNot(
                        request.getEmail(),
                        organizationId,
                        id)) {

            throw new DuplicateResourceException(
                    "Email",
                    request.getEmail());
        }

        if (request.getMobile() != null &&
                repository.existsByMobileAndOrganizationIdAndIdNot(
                        request.getMobile(),
                        organizationId,
                        id)) {

            throw new DuplicateResourceException(
                    "Mobile",
                    request.getMobile());
        }

        if (request.getOrganizationId() != null) {

            Organization organization =
                    organizationRepository.findById(
                                    request.getOrganizationId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Organization",
                                            request.getOrganizationId()));

            teacher.setOrganization(organization);
        }

        if (request.getUserId() != null) {

            if (repository.existsByUserIdAndIdNot(
                    request.getUserId(),
                    id)) {

                throw new BusinessException(
                        "User is already assigned to another Teacher.");
            }

            User user =
                    userRepository.findById(
                                    request.getUserId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "User",
                                            request.getUserId()));

            if (!teacher.getOrganization().getId().equals(
                    user.getOrganization().getId())) {

                throw new BusinessException(
                        "User does not belong to Organization.");
            }

            teacher.setUser(user);
        }

/*        mapper.updateEntity(request, teacher);

        Teacher updated =
                repository.save(teacher);

        return mapper.toResponse(updated);*/
        mapper.updateEntity(request, teacher);
        Teacher updated = repository.save(teacher);
        syncEmployeeFromTeacher(updated);
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherResponse getById(Long id) {

        Teacher teacher =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher",
                                        id));

        return mapper.toResponse(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponse> getAll() {

        return mapper.toResponseList(
                repository.findAll());
    }
    @Override
    @Transactional(readOnly = true)
    public Page<TeacherResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeacherResponse> search(
            TeacherSearchRequest request) {

        Pageable pageable =
                PageRequest.of(
                        request.getPage(),
                        request.getSize(),
                        request.getDirection().equalsIgnoreCase("DESC")
                                ? Sort.by(request.getSortBy()).descending()
                                : Sort.by(request.getSortBy()).ascending());

        Specification<Teacher> specification =
                TeacherSpecification.hasKeyword(request.getKeyword())
                        .and(TeacherSpecification.hasOrganization(request.getOrganizationId()))
                        .and(TeacherSpecification.isActive(request.getActive()));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public TeacherResponse activate(Long id) {

        Teacher teacher = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher", id));

        teacher.setActive(true);
        syncEmployeeFromTeacher(teacher);

        return mapper.toResponse(repository.save(teacher));
    }

    @Override
    public TeacherResponse deactivate(Long id) {

        Teacher teacher = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher", id));

        teacher.setActive(false);
        syncEmployeeFromTeacher(teacher);

        return mapper.toResponse(repository.save(teacher));
    }

    @Override
    public void delete(Long id) {

        Teacher teacher = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher", id));
        teacher.setActive(false);
        repository.save(teacher);
        log.info("Teacher Soft Deleted : {}", id);
    }

    @Override
    @Transactional
    public TeacherPhotoResponse uploadPhoto(
            Long teacherId,
            MultipartFile photo) {

        Teacher teacher = repository.findById(teacherId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher not found with id: " + teacherId
                        )
                );

        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Teacher photo is required"
            );
        }

        String oldPhotoUrl =
                teacher.getPhotoUrl();

        String photoUrl =
                fileStorageService.store(
                        photo,
                        "teachers",
                        teacher.getOrganization().getId()
                );

        teacher.setPhotoUrl(photoUrl);

        repository.save(teacher);

        /*
         * Delete old photo only after the new photo
         * has been successfully stored and database
         * entity has been updated.
         */
        if (oldPhotoUrl != null &&
                !oldPhotoUrl.equals(photoUrl)) {

            fileStorageService.delete(
                    oldPhotoUrl
            );
        }

        return new TeacherPhotoResponse(
                teacher.getId(),
                photoUrl
        );
    }

    @Override
    @Transactional
    public void deletePhoto(Long teacherId) {

        Teacher teacher = repository.findById(teacherId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher not found with id: " + teacherId
                        )
                );

        String photoUrl = teacher.getPhotoUrl();

        if (photoUrl == null || photoUrl.isBlank()) {
            return;
        }

        /*
         * Remove the physical file.
         */
        fileStorageService.delete(photoUrl);

        /*
         * Remove the photo URL from the database.
         */
        teacher.setPhotoUrl(null);

        repository.save(teacher);
    }
    private void syncEmployeeFromTeacher(Teacher teacher) {

        Employee employee = teacher.getEmployee();
        if (employee == null) {
            return;
        }
        employee.setEmployeeCode(teacher.getEmployeeCode());
        employee.setFirstName(teacher.getFirstName());
        employee.setLastName(teacher.getLastName());
        employee.setGender(teacher.getGender());
        employee.setDateOfBirth(teacher.getDateOfBirth());
        employee.setJoiningDate(teacher.getJoiningDate());
        employee.setEmail(teacher.getEmail());
        employee.setMobile(teacher.getMobile());
        employee.setAddress(teacher.getAddress());
        employee.setOrganization(teacher.getOrganization());
        employee.setUser(teacher.getUser());
        employee.setActive(teacher.getActive());

        employeeRepository.save(employee);
    }
}
