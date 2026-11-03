package com.smartexam.staff.document.mapper;

import com.smartexam.staff.document.dto.StaffDocumentRequest;
import com.smartexam.staff.document.dto.StaffDocumentResponse;
import com.smartexam.staff.document.dto.StaffDocumentUpdateRequest;
import com.smartexam.staff.document.entity.StaffDocument;
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
public interface StaffDocumentMapper {

    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "documentType", ignore = true)
    @Mapping(target = "fileName", ignore = true)
    @Mapping(target = "storedFileName", ignore = true)
    @Mapping(target = "filePath", ignore = true)
    @Mapping(target = "fileType", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "verificationStatus", ignore = true)
    @Mapping(target = "verifiedBy", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "verificationComments", ignore = true)
    StaffDocument toEntity(
            StaffDocumentRequest request
    );

    @Mapping(
            source = "organization.id",
            target = "organizationId"
    )
    @Mapping(
            source = "organization.name",
            target = "organizationName"
    )

    @Mapping(
            source = "teacher.id",
            target = "teacherId"
    )
    @Mapping(
            target = "teacherName",
            expression = "java(getTeacherName(entity))"
    )
    @Mapping(
            target = "teacherEmployeeCode",
            expression = "java(getTeacherEmployeeCode(entity))"
    )

    @Mapping(
            source = "documentType.id",
            target = "documentTypeId"
    )
    @Mapping(
            source = "documentType.name",
            target = "documentTypeName"
    )
    @Mapping(
            source = "documentType.code",
            target = "documentTypeCode"
    )

    StaffDocumentResponse toResponse(
            StaffDocument entity
    );

    List<StaffDocumentResponse> toResponseList(
            List<StaffDocument> entities
    );

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "documentType", ignore = true)
    @Mapping(target = "fileName", ignore = true)
    @Mapping(target = "storedFileName", ignore = true)
    @Mapping(target = "filePath", ignore = true)
    @Mapping(target = "fileType", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "verificationStatus", ignore = true)
    @Mapping(target = "verifiedBy", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "verificationComments", ignore = true)
    void updateEntity(
            StaffDocumentUpdateRequest request,
            @MappingTarget StaffDocument entity
    );

    default String getTeacherName(
            StaffDocument entity
    ) {

        if (entity == null ||
                entity.getTeacher() == null) {

            return null;
        }

        String firstName =
                entity.getTeacher().getFirstName();

        String lastName =
                entity.getTeacher().getLastName();

        String first =
                firstName == null ? "" : firstName.trim();

        String last =
                lastName == null ? "" : lastName.trim();

        return (first + " " + last).trim();
    }

    default String getTeacherEmployeeCode(
            StaffDocument entity
    ) {

        if (entity == null ||
                entity.getTeacher() == null) {

            return null;
        }

        return entity.getTeacher().getEmployeeCode();
    }
}