package com.smartexam.organization.mapper;

import com.smartexam.organization.dto.OrganizationRequest;
import com.smartexam.organization.dto.OrganizationResponse;
import com.smartexam.organization.dto.OrganizationUpdateRequest;
import com.smartexam.organization.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    Organization toEntity(
            OrganizationRequest request
    );

    OrganizationResponse toResponse(
            Organization organization
    );

    List<OrganizationResponse> toResponseList(
            List<Organization> organizations
    );

    void updateEntity(
            OrganizationUpdateRequest request,
            @MappingTarget Organization organization
    );
}