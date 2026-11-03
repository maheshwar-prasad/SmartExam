package com.smartexam.subject.service;

import com.smartexam.subject.dto.SubjectRequest;
import com.smartexam.subject.dto.SubjectResponse;
import com.smartexam.subject.dto.SubjectSearchRequest;
import com.smartexam.subject.dto.SubjectUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubjectService {

    SubjectResponse create(
            SubjectRequest request);

    SubjectResponse update(
            Long id,
            SubjectUpdateRequest request);

    SubjectResponse getById(
            Long id);

    List<SubjectResponse> getAll();

    Page<SubjectResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction);

    Page<SubjectResponse> search(
            SubjectSearchRequest request);

    SubjectResponse activate(
            Long id);

    SubjectResponse deactivate(
            Long id);

    void delete(
            Long id);

}