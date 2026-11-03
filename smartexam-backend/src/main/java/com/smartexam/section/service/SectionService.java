package com.smartexam.section.service;

import com.smartexam.section.dto.SectionRequest;
import com.smartexam.section.dto.SectionResponse;
import com.smartexam.section.dto.SectionSearchRequest;
import com.smartexam.section.dto.SectionUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SectionService {

    /**
     * Create Section
     */
    SectionResponse create(
            SectionRequest request);

    /**
     * Update Section
     */
    SectionResponse update(
            Long id,
            SectionUpdateRequest request);

    /**
     * Get Section By Id
     */
    SectionResponse getById(
            Long id);

    /**
     * Get All Sections
     */
    List<SectionResponse> getAll();

    /**
     * Get All Sections With Pagination
     */
    Page<SectionResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction);

    /**
     * Search Sections
     */
    Page<SectionResponse> search(
            SectionSearchRequest request);

    /**
     * Activate Section
     */
    SectionResponse activate(
            Long id);

    /**
     * Deactivate Section
     */
    SectionResponse deactivate(
            Long id);

    /**
     * Soft Delete Section
     */
    void delete(
            Long id);

}