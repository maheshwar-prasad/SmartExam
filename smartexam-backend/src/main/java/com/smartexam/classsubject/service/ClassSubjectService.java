package com.smartexam.classsubject.service;

import com.smartexam.classsubject.dto.ClassSubjectRequest;
import com.smartexam.classsubject.dto.ClassSubjectResponse;
import com.smartexam.classsubject.dto.ClassSubjectSearchRequest;
import com.smartexam.classsubject.dto.ClassSubjectUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassSubjectService {

    ClassSubjectResponse create(
            ClassSubjectRequest request
    );

    ClassSubjectResponse update(
            Long id,
            ClassSubjectUpdateRequest request
    );

    ClassSubjectResponse getById(
            Long id
    );

    List<ClassSubjectResponse> getAll();

    Page<ClassSubjectResponse> getPage(
            int page,
            int size,
            String sortBy,
            String direction
    );

    Page<ClassSubjectResponse> search(
            ClassSubjectSearchRequest request
    );

    ClassSubjectResponse activate(
            Long id
    );

    ClassSubjectResponse deactivate(
            Long id
    );

    void delete(
            Long id
    );
}