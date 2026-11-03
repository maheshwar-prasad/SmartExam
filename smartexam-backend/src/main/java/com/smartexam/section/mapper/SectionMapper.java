package com.smartexam.section.mapper;

import com.smartexam.section.dto.SectionRequest;
import com.smartexam.section.dto.SectionResponse;
import com.smartexam.section.dto.SectionUpdateRequest;
import com.smartexam.section.entity.Section;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SectionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "academicSession", ignore = true)
    @Mapping(target = "classRoom", ignore = true)
    @Mapping(target = "active", constant = "true")
    Section toEntity(SectionRequest request);

    @Mapping(target = "organizationId",
            source = "organization.id")
    @Mapping(target = "organizationName",
            source = "organization.name")
    @Mapping(target = "academicSessionId",
            source = "academicSession.id")
    @Mapping(target = "academicSessionName",
            source = "academicSession.name")
    @Mapping(target = "classRoomId",
            source = "classRoom.id")
    @Mapping(target = "classRoomName",
            source = "classRoom.name")
    SectionResponse toResponse(Section entity);

    List<SectionResponse> toResponseList(List<Section> entities);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "academicSession", ignore = true)
    @Mapping(target = "classRoom", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntity(
            SectionUpdateRequest request,
            @MappingTarget Section entity);

}