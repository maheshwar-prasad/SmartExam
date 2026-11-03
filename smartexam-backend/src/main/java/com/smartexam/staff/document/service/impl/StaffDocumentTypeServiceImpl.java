package com.smartexam.staff.document.service.impl;

import com.smartexam.common.exception.BusinessException;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.staff.document.dto.StaffDocumentTypeRequest;
import com.smartexam.staff.document.dto.StaffDocumentTypeResponse;
import com.smartexam.staff.document.dto.StaffDocumentTypeUpdateRequest;
import com.smartexam.staff.document.entity.StaffDocumentType;
import com.smartexam.staff.document.mapper.StaffDocumentTypeMapper;
import com.smartexam.staff.document.repository.StaffDocumentTypeRepository;
import com.smartexam.staff.document.service.StaffDocumentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffDocumentTypeServiceImpl
        implements StaffDocumentTypeService {

    private final StaffDocumentTypeRepository documentTypeRepository;
    private final StaffDocumentTypeMapper documentTypeMapper;
    private final OrganizationRepository organizationRepository;

    @Override
    public StaffDocumentTypeResponse create(
            StaffDocumentTypeRequest request
    ) {

        if (request == null) {
            throw new BusinessException(
                    "Document type request is required"
            );
        }

        String code = normalizeCode(request.code());

        if (documentTypeRepository
                .existsByOrganizationIdAndCode(
                        request.organizationId(),
                        code
                )) {

            throw new BusinessException(
                    "Document type code already exists: " + code
            );
        }

        Organization organization =
                organizationRepository
                        .findById(request.organizationId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Organization not found: "
                                                + request.organizationId()
                                )
                        );

        StaffDocumentType entity =
                documentTypeMapper.toEntity(request);

        entity.setOrganization(organization);
        entity.setCode(code);

        if (entity.getActive() == null) {
            entity.setActive(true);
        }

        return documentTypeMapper.toResponse(
                documentTypeRepository.save(entity)
        );
    }

    @Override
    public StaffDocumentTypeResponse update(
            Long id,
            StaffDocumentTypeUpdateRequest request
    ) {

        StaffDocumentType entity =
                documentTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Document type not found: " + id
                                )
                        );

        Long organizationId =
                request.organizationId() != null
                        ? request.organizationId()
                        : entity.getOrganization().getId();

        Organization organization =
                organizationRepository
                        .findById(organizationId)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Organization not found: "
                                                + organizationId
                                )
                        );

        String newCode = request.code() != null
                ? normalizeCode(request.code())
                : entity.getCode();

        if (documentTypeRepository
                .existsByOrganizationIdAndCodeAndIdNot(
                        organizationId,
                        newCode,
                        id
                )) {

            throw new BusinessException(
                    "Document type code already exists: "
                            + newCode
            );
        }

        documentTypeMapper.updateEntity(
                request,
                entity
        );

        entity.setOrganization(organization);
        entity.setCode(newCode);

        return documentTypeMapper.toResponse(
                documentTypeRepository.save(entity)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public StaffDocumentTypeResponse getById(
            Long id
    ) {

        StaffDocumentType entity =
                documentTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Document type not found: " + id
                                )
                        );

        return documentTypeMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDocumentTypeResponse> getAll() {

        return documentTypeMapper.toResponseList(
                documentTypeRepository.findAll()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffDocumentTypeResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort.Direction sortDirection =
                "ASC".equalsIgnoreCase(direction)
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortDirection, sortBy)
                );

        return documentTypeRepository
                .findAll(pageable)
                .map(documentTypeMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDocumentTypeResponse> getByOrganization(
            Long organizationId
    ) {

        return documentTypeMapper.toResponseList(
                documentTypeRepository
                        .findByOrganizationId(organizationId)
        );
    }

    @Override
    public StaffDocumentTypeResponse activate(
            Long id
    ) {

        StaffDocumentType entity =
                documentTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Document type not found: " + id
                                )
                        );

        entity.setActive(true);

        return documentTypeMapper.toResponse(
                documentTypeRepository.save(entity)
        );
    }

    @Override
    public StaffDocumentTypeResponse deactivate(
            Long id
    ) {

        StaffDocumentType entity =
                documentTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Document type not found: " + id
                                )
                        );

        entity.setActive(false);

        return documentTypeMapper.toResponse(
                documentTypeRepository.save(entity)
        );
    }

    @Override
    public void delete(Long id) {

        StaffDocumentType entity =
                documentTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Document type not found: " + id
                                )
                        );

        documentTypeRepository.delete(entity);
    }

    private String normalizeCode(String code) {

        if (code == null || code.isBlank()) {
            throw new BusinessException(
                    "Document type code is required"
            );
        }

        return code.trim().toUpperCase();
    }
}