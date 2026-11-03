package com.smartexam.staff.leave.mapper;

import com.smartexam.staff.leave.dto.LeaveTypeRequest;
import com.smartexam.staff.leave.dto.LeaveTypeResponse;
import com.smartexam.staff.leave.dto.LeaveTypeUpdateRequest;
import com.smartexam.staff.leave.entity.LeaveType;
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
public interface LeaveTypeMapper {

    @Mapping(target = "organization", ignore = true)
    LeaveType toEntity(LeaveTypeRequest request);

    @Mapping(
            source = "organization.id",
            target = "organizationId"
    )
    @Mapping(
            source = "organization.name",
            target = "organizationName"
    )
    LeaveTypeResponse toResponse(LeaveType entity);

    List<LeaveTypeResponse> toResponseList(
            List<LeaveType> entities
    );

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "organization", ignore = true)
    void updateEntity(
            LeaveTypeUpdateRequest request,
            @MappingTarget LeaveType entity
    );
}