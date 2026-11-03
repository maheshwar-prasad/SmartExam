package com.smartexam.institution.campuses.mapper;

import com.smartexam.institution.campuses.dto.CampusCreateRequest;
import com.smartexam.institution.campuses.dto.CampusResponse;
import com.smartexam.institution.campuses.dto.CampusUpdateRequest;
import com.smartexam.institution.campuses.entity.Campus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CampusMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Campus toEntity(CampusCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(
            CampusUpdateRequest request,
            @MappingTarget Campus campus
    );

    @Mapping(
            target = "organizationId",
            source = "organization.id"
    )
    CampusResponse toResponse(Campus campus);
}