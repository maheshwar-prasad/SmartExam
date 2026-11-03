package com.smartexam.examregistration.mapper;

import com.smartexam.examregistration.dto.StudentExamRegistrationRequest;
import com.smartexam.examregistration.dto.StudentExamRegistrationResponse;
import com.smartexam.examregistration.entity.StudentExamRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE
)
public interface StudentExamRegistrationMapper {

    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "status",
            ignore = true
    )
    @Mapping(
            target = "active",
            ignore = true
    )
    @Mapping(
            target = "createdAt",
            ignore = true
    )
    @Mapping(
            target = "updatedAt",
            ignore = true
    )
    StudentExamRegistration toEntity(
            StudentExamRegistrationRequest request
    );

    StudentExamRegistrationResponse toResponse(
            StudentExamRegistration registration
    );

    List<StudentExamRegistrationResponse> toResponseList(
            List<StudentExamRegistration> registrations
    );

    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "status",
            ignore = true
    )
    @Mapping(
            target = "active",
            ignore = true
    )
    @Mapping(
            target = "createdAt",
            ignore = true
    )
    @Mapping(
            target = "updatedAt",
            ignore = true
    )
    void updateEntity(
            StudentExamRegistrationRequest request,
            @MappingTarget StudentExamRegistration registration
    );
}