package com.smartexam.academic.service;

import com.smartexam.academic.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AcademicSessionService {

    AcademicSessionResponse create(
            AcademicSessionRequest request);

    AcademicSessionResponse update(
            Long id,
            AcademicSessionUpdateRequest request);

    AcademicSessionResponse getById(Long id);

    List<AcademicSessionResponse> getAll();

    Page<AcademicSessionResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction);

    Page<AcademicSessionResponse> search(
            AcademicSessionSearchRequest request);

    AcademicSessionResponse activate(Long id);

    AcademicSessionResponse deactivate(Long id);

    void delete(Long id);

}