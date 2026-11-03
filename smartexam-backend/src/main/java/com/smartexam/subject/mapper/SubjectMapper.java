package com.smartexam.subject.mapper;

import com.smartexam.subject.dto.SubjectRequest;
import com.smartexam.subject.dto.SubjectResponse;
import com.smartexam.subject.dto.SubjectUpdateRequest;
import com.smartexam.subject.entity.Subject;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SubjectMapper {

    Subject toEntity(SubjectRequest request);

    @Mapping(target = "organizationId",
            source = "organization.id")
    @Mapping(target = "organizationName",
            source = "organization.name")
    @Mapping(target = "academicSessionId",
            source = "academicSession.id")
    @Mapping(target = "academicSessionName",
            source = "academicSession.name")
    SubjectResponse toResponse(Subject entity);

    List<SubjectResponse> toResponseList(
            List<Subject> entities);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(
            SubjectUpdateRequest request,
            @MappingTarget Subject entity);

}