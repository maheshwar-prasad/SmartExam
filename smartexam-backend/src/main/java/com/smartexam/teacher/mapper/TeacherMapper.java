package com.smartexam.teacher.mapper;

import com.smartexam.teacher.dto.TeacherRequest;
import com.smartexam.teacher.dto.TeacherResponse;
import com.smartexam.teacher.dto.TeacherUpdateRequest;
import com.smartexam.teacher.entity.Teacher;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TeacherMapper {

    Teacher toEntity(TeacherRequest request);

    @Mapping(target = "organizationId",
            source = "organization.id")
    @Mapping(target = "organizationName",
            source = "organization.name")

    @Mapping(target = "userId",
            source = "user.id")

    @Mapping(target = "userName",
            expression = "java(entity.getUser().getFirstName() + \" \" + entity.getUser().getLastName())")

    @Mapping(target = "userEmail",
            source = "user.email")
    TeacherResponse toResponse(Teacher entity);

    List<TeacherResponse> toResponseList(
            List<Teacher> entities);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(
            TeacherUpdateRequest request,
            @MappingTarget Teacher entity);

}