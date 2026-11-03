package com.smartexam.teacherSubjectAssignment.service;

import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentRequest;
import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentResponse;
import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentSearchRequest;
import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TeacherSubjectAssignmentService {

    TeacherSubjectAssignmentResponse create(
            TeacherSubjectAssignmentRequest request);

    TeacherSubjectAssignmentResponse update(
            Long id,
            TeacherSubjectAssignmentUpdateRequest request);

    TeacherSubjectAssignmentResponse getById(
            Long id);

    List<TeacherSubjectAssignmentResponse> getAll();

    Page<TeacherSubjectAssignmentResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction);

    Page<TeacherSubjectAssignmentResponse> search(
            TeacherSubjectAssignmentSearchRequest request);

    TeacherSubjectAssignmentResponse activate(
            Long id);

    TeacherSubjectAssignmentResponse deactivate(
            Long id);

    void delete(
            Long id);

}