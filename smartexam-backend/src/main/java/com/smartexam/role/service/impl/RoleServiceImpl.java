package com.smartexam.role.service.impl;

import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.permission.entity.Permission;
import com.smartexam.permission.repository.PermissionRepository;
import com.smartexam.role.dto.RoleRequest;
import com.smartexam.role.dto.RoleResponse;
import com.smartexam.role.dto.RoleUpdateRequest;
import com.smartexam.role.entity.Role;
import com.smartexam.role.mapper.RoleMapper;
import com.smartexam.role.repository.RoleRepository;
import com.smartexam.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    private final RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleRequest request) {

        log.info("Creating Role : {}", request.getName());

        if (roleRepository.existsByName(request.getName())) {

            throw new DuplicateResourceException(
                    "Role",
                    request.getName());

        }

        Role role = roleMapper.toEntity(request);

        Set<Permission> permissions =
                permissionRepository.findAllById(
                                request.getPermissionIds())
                        .stream()
                        .collect(Collectors.toSet());

        role.setPermissions(permissions);

        role.setActive(true);

        Role saved = roleRepository.save(role);

        log.info("Role Created Successfully : {}",
                saved.getId());

        return roleMapper.toResponse(saved);

    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getById(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role",
                                id));

        return roleMapper.toResponse(role);

    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getAll() {

        return roleMapper.toResponseList(
                roleRepository.findAll()
        );

    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleResponse> getAll(int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return roleRepository.findAll(pageable)
                .map(roleMapper::toResponse);

    }

    @Override
    public RoleResponse update(Long id,
            RoleUpdateRequest request) {

        log.info("Updating Role : {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role",
                                id));

        if (!role.getName().equalsIgnoreCase(request.getName())
                && roleRepository.existsByName(request.getName())) {

            throw new DuplicateResourceException(
                    "Role",
                    request.getName());

        }

        roleMapper.updateEntity(request, role);

        if (request.getPermissionIds() != null
                && !request.getPermissionIds().isEmpty()) {

            Set<Permission> permissions =
                    permissionRepository.findAllById(
                                    request.getPermissionIds())
                            .stream()
                            .collect(Collectors.toSet());

            role.setPermissions(permissions);

        }

        Role updated = roleRepository.save(role);

        log.info("Role Updated Successfully : {}", updated.getId());

        return roleMapper.toResponse(updated);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleResponse> search(String keyword,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        return roleRepository.search(keyword, pageable)
                .map(roleMapper::toResponse);

    }

    @Override
    public RoleResponse activate(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role",
                                id));

        role.setActive(true);

        Role updated = roleRepository.save(role);

        return roleMapper.toResponse(updated);

    }

    @Override
    public RoleResponse deactivate(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role",
                                id));

        role.setActive(false);

        Role updated = roleRepository.save(role);

        return roleMapper.toResponse(updated);

    }

    @Override
    public void delete(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role",
                                id));

        role.setActive(false);

        roleRepository.save(role);

        log.info("Role Soft Deleted : {}", id);

    }
}