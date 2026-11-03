package com.smartexam.permission.service.impl;

import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.permission.dto.PermissionRequest;
import com.smartexam.permission.dto.PermissionResponse;
import com.smartexam.permission.dto.PermissionSearchRequest;
import com.smartexam.permission.dto.PermissionUpdateRequest;
import com.smartexam.permission.entity.Permission;
import com.smartexam.permission.mapper.PermissionMapper;
import com.smartexam.permission.repository.PermissionRepository;
import com.smartexam.permission.service.PermissionService;
import com.smartexam.permission.specification.PermissionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repository;

    private final PermissionMapper mapper;

    @Override
    public PermissionResponse create(PermissionRequest request) {

        log.info("Creating Permission : {}", request.getName());

        if (repository.existsByNameIgnoreCase(request.getName())) {

            throw new DuplicateResourceException(
                    "Permission",
                    request.getName());

        }

        Permission permission =
                mapper.toEntity(request);

        permission.setActive(true);

        Permission saved =
                repository.save(permission);

        return mapper.toResponse(saved);

    }

    @Override
    public PermissionResponse update(
            Long id,
            PermissionUpdateRequest request) {

        Permission permission =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Permission",
                                        id));

        if (request.getName() != null &&
                repository.existsByNameIgnoreCaseAndIdNot(
                        request.getName(),
                        id)) {

            throw new DuplicateResourceException(
                    "Permission",
                    request.getName());

        }

        mapper.updateEntity(request, permission);

        Permission updated =
                repository.save(permission);

        return mapper.toResponse(updated);

    }

    @Override
    @Transactional(readOnly = true)
    public PermissionResponse getById(Long id) {

        Permission permission =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Permission",
                                        id));

        return mapper.toResponse(permission);

    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> getAll() {

        return mapper.toResponseList(
                repository.findAll());

    }

    @Override
    @Transactional(readOnly = true)
    public Page<PermissionResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return repository.findAll(pageable)
                .map(mapper::toResponse);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<PermissionResponse> search(
            PermissionSearchRequest request) {

        Pageable pageable =
                PageRequest.of(
                        request.getPage(),
                        request.getSize(),
                        request.getDirection().equalsIgnoreCase("DESC")
                                ? Sort.by(request.getSortBy()).descending()
                                : Sort.by(request.getSortBy()).ascending());

        Specification<Permission> specification =
                (root, query, cb) -> cb.conjunction();

        specification = specification.and(
                PermissionSpecification.hasKeyword(
                        request.getKeyword()));

        specification = specification.and(
                PermissionSpecification.hasModule(
                        request.getModule()));

        specification = specification.and(
                PermissionSpecification.hasOperation(
                        request.getOperation()));

        specification = specification.and(
                PermissionSpecification.isActive(
                        request.getActive()));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);

    }

    @Override
    public PermissionResponse activate(Long id) {

        Permission permission =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Permission",
                                        id));

        permission.setActive(true);

        return mapper.toResponse(
                repository.save(permission));

    }

    @Override
    public PermissionResponse deactivate(Long id) {

        Permission permission =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Permission",
                                        id));

        permission.setActive(false);

        return mapper.toResponse(
                repository.save(permission));

    }

    @Override
    public void delete(Long id) {

        Permission permission =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Permission",
                                        id));

        permission.setActive(false);

        repository.save(permission);

        log.info("Permission deleted successfully : {}", id);

    }

}