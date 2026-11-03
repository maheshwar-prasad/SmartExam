package com.smartexam.institution.course.controller;

import com.smartexam.institution.course.dto.CourseCreateRequest;
import com.smartexam.institution.course.dto.CourseResponse;
import com.smartexam.institution.course.dto.CourseUpdateRequest;
import com.smartexam.institution.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseCreateRequest request
    ) {
        CourseResponse response = courseService.createCourse(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponse> getCourse(
            @PathVariable Long courseId,
            @RequestParam Long organizationId
    ) {
        return ResponseEntity.ok(
                courseService.getCourse(courseId, organizationId)
        );
    }

    @GetMapping
    public ResponseEntity<Page<CourseResponse>> getCourses(
            @RequestParam Long organizationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        String safeSortBy = resolveSortField(sortBy);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, safeSortBy)
        );

        return ResponseEntity.ok(
                courseService.getCourses(organizationId, pageable)
        );
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long courseId,
            @RequestParam Long organizationId,
            @Valid @RequestBody CourseUpdateRequest request
    ) {
        return ResponseEntity.ok(
                courseService.updateCourse(
                        courseId,
                        organizationId,
                        request
                )
        );
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long courseId,
            @RequestParam Long organizationId
    ) {
        courseService.deleteCourse(courseId, organizationId);

        return ResponseEntity.noContent().build();
    }

    private String resolveSortField(String sortBy) {

        return switch (sortBy) {
            case "id",
                 "name",
                 "code",
                 "credits",
                 "semester",
                 "courseType",
                 "active" -> sortBy;

            default -> "name";
        };
    }
}