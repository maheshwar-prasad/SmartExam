package com.smartexam.organization.service;

import com.smartexam.organization.dto.OrganizationRequest;
import com.smartexam.organization.dto.OrganizationResponse;
import com.smartexam.organization.dto.OrganizationUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrganizationService {

    OrganizationResponse create(OrganizationRequest request);

    OrganizationResponse getById(Long id);

    List<OrganizationResponse> getAll();

    Page<OrganizationResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction);

    OrganizationResponse update(
            Long id,
            OrganizationUpdateRequest request);

    void delete(Long id);

    void activate(Long id);

    void deactivate(Long id);

    long countActive();
}