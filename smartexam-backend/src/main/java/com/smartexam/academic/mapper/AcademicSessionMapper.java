package com.smartexam.academic.mapper;

import com.smartexam.academic.dto.AcademicSessionRequest;
import com.smartexam.academic.dto.AcademicSessionResponse;
import com.smartexam.academic.dto.AcademicSessionUpdateRequest;
import com.smartexam.academic.entity.AcademicSession;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AcademicSessionMapper {

    /**
     * Request -> Entity
     */
    @Mapping(target = "organization", ignore = true)
    AcademicSession toEntity(
            AcademicSessionRequest request
    );


    /**
     * Entity -> Response
     */
    @Mapping(
            source = "organization.id",
            target = "organizationId"
    )
    @Mapping(
            source = "organization.name",
            target = "organizationName"
    )
    AcademicSessionResponse toResponse(
            AcademicSession entity
    );


    /**
     * Entity List -> Response List
     */
    List<AcademicSessionResponse> toResponseList(
            List<AcademicSession> entities
    );


    /**
     * Update Existing Entity
     */
    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(
            target = "organization",
            ignore = true
    )
    void updateEntity(
            AcademicSessionUpdateRequest request,
            @MappingTarget AcademicSession entity
    );

}