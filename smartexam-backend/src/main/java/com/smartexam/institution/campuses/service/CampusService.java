package com.smartexam.institution.campuses.service;

import com.smartexam.institution.campuses.dto.CampusCreateRequest;
import com.smartexam.institution.campuses.dto.CampusResponse;
import com.smartexam.institution.campuses.dto.CampusUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampusService {

    CampusResponse createCampus(
            CampusCreateRequest request
    );

    CampusResponse updateCampus(
            Long id,
            CampusUpdateRequest request
    );

    CampusResponse getCampusById(
            Long id
    );

    List<CampusResponse> getAllCampuses(
    );

    Page<CampusResponse> getCampusesPage(
            Long organizationId,
            Pageable pageable
    );

    CampusResponse activateCampus(
            Long id
    );

    CampusResponse deactivateCampus(
            Long id
    );

    void deleteCampus(
            Long id
    );


    List<CampusResponse> getCampusesByOrganization(
            Long organizationId
    );

}