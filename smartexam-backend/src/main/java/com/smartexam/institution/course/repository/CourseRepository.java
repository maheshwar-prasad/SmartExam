package com.smartexam.institution.course.repository;

import com.smartexam.institution.course.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByOrganizationId(
            Long organizationId,
            Pageable pageable
    );

    Optional<Course> findByIdAndOrganizationId(
            Long id,
            Long organizationId
    );

    boolean existsByCodeAndOrganizationId(
            String code,
            Long organizationId
    );

    boolean existsByCodeAndOrganizationIdAndIdNot(
            String code,
            Long organizationId,
            Long id
    );
}