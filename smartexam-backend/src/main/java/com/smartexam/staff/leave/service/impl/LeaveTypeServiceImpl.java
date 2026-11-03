package com.smartexam.staff.leave.service.impl;
import com.smartexam.staff.leave.dto.LeaveTypeRequest;
import com.smartexam.staff.leave.dto.LeaveTypeResponse;
import com.smartexam.staff.leave.dto.LeaveTypeUpdateRequest;
import com.smartexam.staff.leave.entity.LeaveType;
import com.smartexam.staff.leave.mapper.LeaveTypeMapper;
import com.smartexam.staff.leave.repository.LeaveTypeRepository;
import com.smartexam.staff.leave.service.LeaveTypeService;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveTypeServiceImpl implements LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveTypeMapper leaveTypeMapper;
    private final OrganizationRepository organizationRepository;


    @Override
    public LeaveTypeResponse create(LeaveTypeRequest request) {

        validateDuplicateCode(
                request.organizationId(),
                request.code(),
                null
        );

        Organization organization =
                organizationRepository.findById(request.organizationId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Organization not found: "
                                                + request.organizationId()
                                )
                        );

        LeaveType entity =
                leaveTypeMapper.toEntity(request);

        entity.setOrganization(organization);

        if (entity.getDaysPerYear() == null) {
            entity.setDaysPerYear(
                    java.math.BigDecimal.ZERO
            );
        }

        if (entity.getCarryForwardAllowed() == null) {
            entity.setCarryForwardAllowed(false);
        }

        if (entity.getRequiresApproval() == null) {
            entity.setRequiresApproval(true);
        }

        if (entity.getActive() == null) {
            entity.setActive(true);
        }

        LeaveType saved =
                leaveTypeRepository.save(entity);

        return leaveTypeMapper.toResponse(saved);
    }


    @Override
    public LeaveTypeResponse update(
            Long id,
            LeaveTypeUpdateRequest request
    ) {

        LeaveType entity =
                leaveTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave type not found: " + id
                                )
                        );

        Long organizationId =
                request.organizationId() != null
                        ? request.organizationId()
                        : entity.getOrganization().getId();

        validateDuplicateCode(
                organizationId,
                request.code(),
                id
        );

        Organization organization =
                organizationRepository.findById(organizationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Organization not found: "
                                                + organizationId
                                )
                        );

        leaveTypeMapper.updateEntity(
                request,
                entity
        );

        entity.setOrganization(organization);

        LeaveType updated =
                leaveTypeRepository.save(entity);

        return leaveTypeMapper.toResponse(updated);
    }


    @Override
    @Transactional(readOnly = true)
    public LeaveTypeResponse getById(Long id) {

        LeaveType entity =
                leaveTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave type not found: " + id
                                )
                        );

        return leaveTypeMapper.toResponse(entity);
    }


    @Override
    @Transactional(readOnly = true)
    public List<LeaveTypeResponse> getAll() {

        List<LeaveType> entities =
                leaveTypeRepository.findAll(
                        Sort.by(
                                Sort.Direction.ASC,
                                "name"
                        )
                );

        return leaveTypeMapper.toResponseList(
                entities
        );
    }


    @Override
    @Transactional(readOnly = true)
    public Page<LeaveTypeResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort.Direction sortDirection =
                "DESC".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortDirection, sortBy)
                );

        Page<LeaveType> result =
                leaveTypeRepository.findAll(pageable);

        return result.map(
                leaveTypeMapper::toResponse
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<LeaveTypeResponse> getByOrganization(
            Long organizationId
    ) {

        return leaveTypeMapper.toResponseList(
                leaveTypeRepository
                        .findByOrganizationId(organizationId)
        );
    }


    @Override
    public LeaveTypeResponse activate(Long id) {

        LeaveType entity =
                leaveTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave type not found: " + id
                                )
                        );

        entity.setActive(true);

        return leaveTypeMapper.toResponse(
                leaveTypeRepository.save(entity)
        );
    }


    @Override
    public LeaveTypeResponse deactivate(Long id) {

        LeaveType entity =
                leaveTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave type not found: " + id
                                )
                        );

        entity.setActive(false);

        return leaveTypeMapper.toResponse(
                leaveTypeRepository.save(entity)
        );
    }


    @Override
    public void delete(Long id) {

        LeaveType entity =
                leaveTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave type not found: " + id
                                )
                        );

        leaveTypeRepository.delete(entity);
    }


    private void validateDuplicateCode(
            Long organizationId,
            String code,
            Long id
    ) {

        if (code == null || code.isBlank()) {
            return;
        }

        String normalizedCode =
                code.trim().toUpperCase();

        if (id == null) {

            if (leaveTypeRepository
                    .existsByOrganizationIdAndCode(
                            organizationId,
                            normalizedCode
                    )) {

                throw new IllegalArgumentException(
                        "Leave type code already exists: "
                                + normalizedCode
                );
            }

        } else {

            if (leaveTypeRepository
                    .existsByOrganizationIdAndCodeAndIdNot(
                            organizationId,
                            normalizedCode,
                            id
                    )) {

                throw new IllegalArgumentException(
                        "Leave type code already exists: "
                                + normalizedCode
                );
            }
        }
    }
}
