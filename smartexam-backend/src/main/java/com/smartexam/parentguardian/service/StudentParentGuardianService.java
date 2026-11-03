package com.smartexam.parentguardian.service;

import com.smartexam.parentguardian.dto.StudentParentGuardianRequest;
import com.smartexam.parentguardian.dto.StudentParentGuardianResponse;

import java.util.List;

public interface StudentParentGuardianService {

    StudentParentGuardianResponse create(
            StudentParentGuardianRequest request
    );

    StudentParentGuardianResponse update(
            Long id,
            StudentParentGuardianRequest request
    );

    List<StudentParentGuardianResponse> getByStudentId(
            Long studentId
    );

    StudentParentGuardianResponse getById(
            Long id
    );

    void deactivate(
            Long id
    );

    void activate(
            Long id
    );
}