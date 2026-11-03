package com.smartexam.staff.document.mapper;

import com.smartexam.staff.document.dto.StaffDocumentTypeRequest;
import com.smartexam.staff.document.dto.StaffDocumentTypeResponse;
import com.smartexam.staff.document.dto.StaffDocumentTypeUpdateRequest;
import com.smartexam.staff.document.entity.StaffDocumentType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StaffDocumentTypeMapper {

    @Mapping(target = "organization", ignore = true)
    StaffDocumentType toEntity(
            StaffDocumentTypeRequest request
    );

    @Mapping(
            source = "organization.id",
            target = "organizationId"
    )
    @Mapping(
            source = "organization.name",
            target = "organizationName"
    )
    StaffDocumentTypeResponse toResponse(
            StaffDocumentType entity
    );

    List<StaffDocumentTypeResponse> toResponseList(
            List<StaffDocumentType> entities
    );

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "organization", ignore = true)
    void updateEntity(
            StaffDocumentTypeUpdateRequest request,
            @MappingTarget StaffDocumentType entity
    );
}