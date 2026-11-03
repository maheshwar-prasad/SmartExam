package com.smartexam.studentenrollment.mapper;

import com.smartexam.studentenrollment.dto.StudentEnrollmentRequest;
import com.smartexam.studentenrollment.dto.StudentEnrollmentResponse;
import com.smartexam.studentenrollment.dto.StudentEnrollmentUpdateRequest;
import com.smartexam.studentenrollment.entity.StudentEnrollment;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StudentEnrollmentMapper {

    StudentEnrollment toEntity(
            StudentEnrollmentRequest request);

    @Mapping(target = "studentId",
            source = "student.id")
    @Mapping(target = "studentCode",
            source = "student.studentCode")
    @Mapping(target = "admissionNo",
            source = "student.admissionNo")
    @Mapping(target = "studentName",
            expression = "java(entity.getStudent().getFirstName() + \" \" + entity.getStudent().getLastName())")

    // =====================================================
    // ORGANIZATION
    // =====================================================

    @Mapping(
            source = "student.organization.id",
            target = "organizationId"
    )

    @Mapping(
            source = "student.organization.name",
            target = "organizationName"
    )


    @Mapping(target = "academicSessionId",
            source = "academicSession.id")
    @Mapping(target = "academicSessionName",
            source = "academicSession.name")

    @Mapping(target = "classRoomId",
            source = "classRoom.id")
    @Mapping(target = "classRoomCode",
            source = "classRoom.code")
    @Mapping(target = "classRoomName",
            source = "classRoom.name")

    @Mapping(target = "sectionId",
            source = "section.id")
    @Mapping(target = "sectionCode",
            source = "section.code")
    @Mapping(target = "sectionName",
            source = "section.name")

    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")

    StudentEnrollmentResponse toResponse(
            StudentEnrollment entity);

    List<StudentEnrollmentResponse> toResponseList(
            List<StudentEnrollment> entities);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(
            StudentEnrollmentUpdateRequest request,
            @MappingTarget StudentEnrollment entity);

}