package com.smartexam.role.service;

import com.smartexam.role.dto.RoleRequest;
import com.smartexam.role.dto.RoleResponse;
import com.smartexam.role.dto.RoleUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {

    RoleResponse create(RoleRequest request);

    RoleResponse update(Long id,
            RoleUpdateRequest request);

    RoleResponse getById(Long id);

    List<RoleResponse> getAll();

    Page<RoleResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction
    );

    Page<RoleResponse> search(
            String keyword,
            int page,
            int size
    );

    RoleResponse activate(Long id);

    RoleResponse deactivate(Long id);

    void delete(Long id);

}