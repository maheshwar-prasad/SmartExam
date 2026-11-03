package com.smartexam.staff.leave.mapper;
import com.smartexam.staff.leave.dto.LeaveBalanceRequest;
import com.smartexam.staff.leave.dto.LeaveBalanceResponse;
import com.smartexam.staff.leave.dto.LeaveBalanceUpdateRequest;
import com.smartexam.staff.leave.entity.LeaveBalance;
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
public interface LeaveBalanceMapper {

    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "leaveType", ignore = true)
    @Mapping(target = "academicSession", ignore = true)
    LeaveBalance toEntity(LeaveBalanceRequest request);

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
    LeaveBalanceResponse toResponse(LeaveBalance entity);

    List<LeaveBalanceResponse> toResponseList(
            List<LeaveBalance> entities
    );

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "leaveType", ignore = true)
    @Mapping(target = "academicSession", ignore = true)
    void updateEntity(
            LeaveBalanceUpdateRequest request,
            @MappingTarget LeaveBalance entity
    );

    default String getTeacherName(LeaveBalance entity) {

        if (entity == null || entity.getTeacher() == null) {
            return null;
        }

        String firstName = entity.getTeacher().getFirstName();
        String lastName = entity.getTeacher().getLastName();

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
            LeaveBalance entity
    ) {

        if (entity == null || entity.getTeacher() == null) {
            return null;
        }

        return entity.getTeacher().getEmployeeCode();
    }
}