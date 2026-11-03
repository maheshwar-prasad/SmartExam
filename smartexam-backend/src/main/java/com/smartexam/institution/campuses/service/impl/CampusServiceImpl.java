package com.smartexam.institution.campuses.service.impl;

import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.institution.campuses.dto.CampusCreateRequest;
import com.smartexam.institution.campuses.dto.CampusResponse;
import com.smartexam.institution.campuses.dto.CampusUpdateRequest;
import com.smartexam.institution.campuses.entity.Campus;
import com.smartexam.institution.campuses.mapper.CampusMapper;
import com.smartexam.institution.campuses.repository.CampusRepository;
import com.smartexam.institution.campuses.service.CampusService;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CampusServiceImpl
        implements CampusService {

    private final CampusRepository campusRepository;
    private final CampusMapper campusMapper;
    private final OrganizationRepository organizationRepository;
    @Override
    @Transactional
    public CampusResponse createCampus(
            CampusCreateRequest request
    ) {
        Organization organization =
                organizationRepository.findById(request.organizationId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organization not found with id: "
                                                + request.organizationId()
                                )
                        );

        Campus campus = new Campus();

        campus.setOrganization(organization);
        campus.setName(request.name());
        campus.setCode(request.code());
        campus.setAddress(request.address());
        campus.setCity(request.city());
        campus.setState(request.state());
        campus.setPostalCode(request.postalCode());
        campus.setContactNumber(request.contactNumber());
        campus.setEmail(request.email());
        campus.setDescription(request.description());
        campus.setActive(
                request.active() == null || request.active()
        );

        Campus savedCampus = campusRepository.save(campus);

        return campusMapper.toResponse(savedCampus);
    }

    @Override
    @Transactional
    public CampusResponse updateCampus(
            Long campusId,
            CampusUpdateRequest request
    ) {
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Campus not found with id: " + campusId
                        )
                );

        campus.setName(request.name());
        campus.setCode(request.code());
        campus.setAddress(request.address());
        campus.setCity(request.city());
        campus.setState(request.state());
        campus.setPostalCode(request.postalCode());
        campus.setContactNumber(request.contactNumber());
        campus.setEmail(request.email());
        campus.setDescription(request.description());

        if (request.active() != null) {
            campus.setActive(request.active());
        }

        Campus updatedCampus = campusRepository.save(campus);

        return campusMapper.toResponse(updatedCampus);
    }

    @Override
    @Transactional(readOnly = true)
    public CampusResponse getCampusById(
            Long id
    ) {
        Campus campus =
                campusRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Campus not found with id: " + id
                                )
                        );

        return toResponse(campus);
    }

    @Override
    public List<CampusResponse> getAllCampuses() {
        return campusRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

//    public List<CampusResponse> getAllCampuses(
//            Long organizationId
//    ) {
//
//        return campusRepository
//                .findAll()
//                .stream()
//                .map(this::toResponse)
//                .toList();
//    }

    @Override
    @Transactional(readOnly = true)
    public Page<CampusResponse> getCampusesPage(
            Long organizationId,
            Pageable pageable
    ) {
        Long validOrganizationId =
                requireOrganizationId(organizationId);

        return campusRepository
                .findByOrganizationId(
                        validOrganizationId,
                        pageable
                )
                .map(this::toResponse);
    }

    @Override
    public CampusResponse activateCampus(
            Long id
    ) {
        Campus campus =
                campusRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Campus not found with id: " + id
                                )
                        );

        campus.setActive(true);

        return toResponse(
                campusRepository.save(campus)
        );
    }

    @Override
    public CampusResponse deactivateCampus(
            Long id
    ) {
        Campus campus =
                campusRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Campus not found with id: " + id
                                )
                        );

        campus.setActive(false);

        return toResponse(
                campusRepository.save(campus)
        );
    }

    @Override
    public void deleteCampus(
            Long id
    ) {
        Campus campus =
                campusRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Campus not found with id: " + id
                                )
                        );

        /*
         * Soft delete.
         */
        campus.setActive(false);

        campusRepository.save(campus);
    }



    private Long requireOrganizationId(
            Long organizationId
    ) {
        if (organizationId == null) {
            throw new IllegalArgumentException(
                    "Organization ID is required."
            );
        }

        return organizationId;
    }

    private String normalizeCode(
            String code
    ) {
        return code
                .trim()
                .toUpperCase();
    }

    private CampusResponse toResponse(
            Campus campus
    ) {
        return new CampusResponse(
                campus.getId(),
                campus.getOrganization().getId(),
                campus.getName(),
                campus.getCode(),
                campus.getAddress(),
                campus.getCity(),
                campus.getState(),
                campus.getPostalCode(),
                campus.getContactNumber(),
                campus.getEmail(),
                campus.getDescription(),
                campus.getActive(),
                campus.getCreatedAt(),
                campus.getUpdatedAt(),
                campus.getCreatedBy(),
                campus.getUpdatedBy()
        );
    }
    @Override
    public List<CampusResponse> getCampusesByOrganization(Long organizationId) {
        return List.of();
    }

}