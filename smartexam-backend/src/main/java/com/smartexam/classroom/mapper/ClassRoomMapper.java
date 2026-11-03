package com.smartexam.classroom.mapper;

import com.smartexam.classroom.dto.ClassRoomRequest;
import com.smartexam.classroom.dto.ClassRoomResponse;
import com.smartexam.classroom.dto.ClassRoomUpdateRequest;
import com.smartexam.classroom.entity.ClassRoom;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ClassRoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "academicSession", ignore = true)
    @Mapping(target = "active", constant = "true")
    ClassRoom toEntity(ClassRoomRequest request);

    @Mapping(target = "organizationId",
            source = "organization.id")
    @Mapping(target = "organizationName",
            source = "organization.name")
    @Mapping(target = "academicSessionId",
            source = "academicSession.id")
    @Mapping(target = "academicSessionName",
            source = "academicSession.name")
    ClassRoomResponse toResponse(ClassRoom entity);

    List<ClassRoomResponse> toResponseList(List<ClassRoom> entities);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "academicSession", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntity(
            ClassRoomUpdateRequest request,
            @MappingTarget ClassRoom entity);

}