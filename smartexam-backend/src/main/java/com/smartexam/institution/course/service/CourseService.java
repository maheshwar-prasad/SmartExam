package com.smartexam.institution.course.service;

import com.smartexam.institution.course.dto.CourseCreateRequest;
import com.smartexam.institution.course.dto.CourseResponse;
import com.smartexam.institution.course.dto.CourseUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

    CourseResponse createCourse(CourseCreateRequest request);

    CourseResponse getCourse(
            Long courseId,
            Long organizationId
    );

    Page<CourseResponse> getCourses(
            Long organizationId,
            Pageable pageable
    );

    CourseResponse updateCourse(
            Long courseId,
            Long organizationId,
            CourseUpdateRequest request
    );

    void deleteCourse(
            Long courseId,
            Long organizationId
    );
}