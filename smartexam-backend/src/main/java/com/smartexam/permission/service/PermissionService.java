package com.smartexam.permission.service;

import com.smartexam.permission.dto.PermissionRequest;
import com.smartexam.permission.dto.PermissionResponse;
import com.smartexam.permission.dto.PermissionSearchRequest;
import com.smartexam.permission.dto.PermissionUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PermissionService {

    PermissionResponse create(PermissionRequest request);

    PermissionResponse update(
            Long id,
            PermissionUpdateRequest request);

    PermissionResponse getById(Long id);

    List<PermissionResponse> getAll();

    Page<PermissionResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction);

    Page<PermissionResponse> search(
            PermissionSearchRequest request);

    PermissionResponse activate(Long id);

    PermissionResponse deactivate(Long id);

    void delete(Long id);

}