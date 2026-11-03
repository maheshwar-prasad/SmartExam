package com.smartexam.studentenrollment.service;

import com.smartexam.studentenrollment.dto.StudentEnrollmentRequest;
import com.smartexam.studentenrollment.dto.StudentEnrollmentResponse;
import com.smartexam.studentenrollment.dto.StudentEnrollmentSearchRequest;
import com.smartexam.studentenrollment.dto.StudentEnrollmentUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentEnrollmentService {

    StudentEnrollmentResponse create(
            StudentEnrollmentRequest request);

    StudentEnrollmentResponse update(
            Long id,
            StudentEnrollmentUpdateRequest request);

    StudentEnrollmentResponse getById(
            Long id);

    List<StudentEnrollmentResponse> getAll();

    Page<StudentEnrollmentResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction);

    Page<StudentEnrollmentResponse> search(
            StudentEnrollmentSearchRequest request);

    StudentEnrollmentResponse activate(
            Long id);

    StudentEnrollmentResponse deactivate(
            Long id);

    void delete(
            Long id);

}