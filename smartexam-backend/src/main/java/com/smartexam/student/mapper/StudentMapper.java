package com.smartexam.student.mapper;

import com.smartexam.student.dto.StudentRequest;
import com.smartexam.student.dto.StudentResponse;
import com.smartexam.student.dto.StudentUpdateRequest;
import com.smartexam.student.entity.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StudentMapper {

    /**
     * Request -> Entity
     */
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "classRoom", ignore = true)
    @Mapping(target = "section", ignore = true)
    Student toEntity(StudentRequest request);


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
    @Mapping(
            source = "classRoom.id",
            target = "classId"
    )
    @Mapping(
            source = "classRoom.name",
            target = "className"
    )
    @Mapping(
            source = "section.id",
            target = "sectionId"
    )
    @Mapping(
            source = "section.name",
            target = "sectionName"
    )
    StudentResponse toResponse(Student student);


    /**
     * Entity List -> DTO List
     */
    List<StudentResponse> toResponseList(
            List<Student> students
    );


    /**
     * Update Existing Entity
     */
    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "classRoom", ignore = true)
    @Mapping(target = "section", ignore = true)
    void updateEntity(
            StudentUpdateRequest request,
            @MappingTarget Student student
    );
}