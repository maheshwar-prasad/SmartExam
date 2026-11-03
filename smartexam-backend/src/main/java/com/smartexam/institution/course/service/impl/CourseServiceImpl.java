package com.smartexam.institution.course.service.impl;

import com.smartexam.institution.course.dto.CourseCreateRequest;
import com.smartexam.institution.course.dto.CourseResponse;
import com.smartexam.institution.course.dto.CourseUpdateRequest;
import com.smartexam.institution.course.entity.Course;
import com.smartexam.institution.course.exception.CourseNotFoundException;
import com.smartexam.institution.course.repository.CourseRepository;
import com.smartexam.institution.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public CourseResponse createCourse(CourseCreateRequest request) {

        if (courseRepository.existsByCodeAndOrganizationId(
                request.code(),
                request.organizationId()
        )) {
            throw new IllegalArgumentException(
                    "Course code already exists: " + request.code()
            );
        }

        Course course = new Course();

        course.setOrganizationId(request.organizationId());
        course.setCampusId(request.campusId());
        course.setDepartmentId(request.departmentId());
        course.setProgramId(request.programId());
        course.setName(request.name());
        course.setCode(request.code());
        course.setDescription(request.description());
        course.setCredits(request.credits());
        course.setSemester(request.semester());
        course.setCourseType(request.courseType());
        course.setActive(
                request.active() == null || request.active()
        );

        Course savedCourse = courseRepository.save(course);

        return mapToResponse(savedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourse(
            Long courseId,
            Long organizationId
    ) {
        Course course = courseRepository
                .findByIdAndOrganizationId(courseId, organizationId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        return mapToResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> getCourses(
            Long organizationId,
            Pageable pageable
    ) {
        return courseRepository
                .findByOrganizationId(organizationId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public CourseResponse updateCourse(
            Long courseId,
            Long organizationId,
            CourseUpdateRequest request
    ) {
        Course course = courseRepository
                .findByIdAndOrganizationId(courseId, organizationId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        if (courseRepository.existsByCodeAndOrganizationIdAndIdNot(
                request.code(),
                organizationId,
                courseId
        )) {
            throw new IllegalArgumentException(
                    "Course code already exists: " + request.code()
            );
        }

        course.setCampusId(request.campusId());
        course.setDepartmentId(request.departmentId());
        course.setProgramId(request.programId());
        course.setName(request.name());
        course.setCode(request.code());
        course.setDescription(request.description());
        course.setCredits(request.credits());
        course.setSemester(request.semester());
        course.setCourseType(request.courseType());

        if (request.active() != null) {
            course.setActive(request.active());
        }

        Course updatedCourse = courseRepository.save(course);

        return mapToResponse(updatedCourse);
    }

    @Override
    public void deleteCourse(
            Long courseId,
            Long organizationId
    ) {
        Course course = courseRepository
                .findByIdAndOrganizationId(courseId, organizationId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        courseRepository.delete(course);
    }

    private CourseResponse mapToResponse(Course course) {

        return new CourseResponse(
                course.getId(),
                course.getOrganizationId(),
                course.getCampusId(),
                course.getDepartmentId(),
                course.getProgramId(),
                course.getName(),
                course.getCode(),
                course.getDescription(),
                course.getCredits(),
                course.getSemester(),
                course.getCourseType(),
                course.getActive()
        );
    }
}