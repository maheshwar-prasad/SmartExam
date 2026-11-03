package com.smartexam.student.service;

import com.smartexam.student.dto.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {

    StudentResponse create(StudentRequest request);

    StudentResponse update(
            Long id,
            StudentUpdateRequest request
    );

    StudentResponse getById(Long id);

    List<StudentResponse> getAll();

    Page<@NotNull StudentResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction
    );

    Page<@NotNull StudentResponse> search(
            StudentSearchRequest request
    );

    StudentResponse activate(Long id);

    StudentResponse deactivate(Long id);

    void delete(Long id);

}