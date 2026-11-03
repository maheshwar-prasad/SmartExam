package com.smartexam.parentguardian.service;

import com.smartexam.parentguardian.dto.ParentGuardianRequest;
import com.smartexam.parentguardian.dto.ParentGuardianResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParentGuardianService {

    ParentGuardianResponse create(
            ParentGuardianRequest request
    );

    ParentGuardianResponse update(
            Long id,
            ParentGuardianRequest request
    );

    ParentGuardianResponse getById(
            Long id
    );

    Page<ParentGuardianResponse> getAll(
            Pageable pageable
    );

    void deactivate(
            Long id
    );

    void activate(
            Long id
    );
}