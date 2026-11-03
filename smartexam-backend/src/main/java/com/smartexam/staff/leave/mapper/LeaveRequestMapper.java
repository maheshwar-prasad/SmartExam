package com.smartexam.staff.leave.mapper;
import com.smartexam.staff.leave.dto.LeaveRequestRequest;
import com.smartexam.staff.leave.dto.LeaveRequestResponse;
import com.smartexam.staff.leave.dto.LeaveRequestUpdateRequest;
import com.smartexam.staff.leave.entity.LeaveRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LeaveRequestMapper {

    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "leaveType", ignore = true)
    @Mapping(target = "academicSession", ignore = true)
    @Mapping(target = "numberOfDays", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "appliedAt", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    @Mapping(target = "rejectedBy", ignore = true)
    @Mapping(target = "rejectedAt", ignore = true)
    @Mapping(target = "approvalComments", ignore = true)
    LeaveRequest toEntity(LeaveRequestRequest request);

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
            source = "leaveType.id",
            target = "leaveTypeId"
    )
    @Mapping(
            source = "leaveType.name",
            target = "leaveTypeName"
    )
    @Mapping(
            source = "leaveType.code",
            target = "leaveTypeCode"
    )
    @Mapping(
            source = "academicSession.id",
            target = "academicSessionId"
    )
    @Mapping(
            source = "academicSession.name",
            target = "academicSessionName"
    )
    LeaveRequestResponse toResponse(LeaveRequest entity);

    List<LeaveRequestResponse> toResponseList(
            List<LeaveRequest> entities
    );

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "leaveType", ignore = true)
    @Mapping(target = "academicSession", ignore = true)
    @Mapping(target = "numberOfDays", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "appliedAt", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    @Mapping(target = "rejectedBy", ignore = true)
    @Mapping(target = "rejectedAt", ignore = true)
    @Mapping(target = "approvalComments", ignore = true)
    void updateEntity(
            LeaveRequestUpdateRequest request,
            @MappingTarget LeaveRequest entity
    );

    default String getTeacherName(LeaveRequest entity) {

        if (entity == null || entity.getTeacher() == null) {
            return null;
        }

        String firstName =
                entity.getTeacher().getFirstName();

        String lastName =
                entity.getTeacher().getLastName();

        StringBuilder name = new StringBuilder();

        if (firstName != null && !firstName.isBlank()) {
            name.append(firstName.trim());
        }

        if (lastName != null && !lastName.isBlank()) {

            if (!name.isEmpty()) {
                name.append(" ");
            }

            name.append(lastName.trim());
        }

        return name.isEmpty()
                ? null
                : name.toString();
    }

    default String getTeacherEmployeeCode(
            LeaveRequest entity
    ) {

        if (entity == null || entity.getTeacher() == null) {
            return null;
        }

        return entity.getTeacher().getEmployeeCode();
    }
}