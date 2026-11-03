package com.smartexam.alumni.service;

import com.smartexam.alumni.dto.AlumniRequest;
import com.smartexam.alumni.dto.AlumniResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlumniService {

    AlumniResponse create(AlumniRequest request);

    AlumniResponse update(
            Long id,
            AlumniRequest request
    );

    AlumniResponse getById(Long id);

    Page<AlumniResponse> getAll(Pageable pageable);

    void activate(Long id);

    void deactivate(Long id);
}