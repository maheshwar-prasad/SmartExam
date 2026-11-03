package com.smartexam.staff.directory.mapper;

import com.smartexam.staff.directory.dto.StaffCreateRequest;
import com.smartexam.staff.directory.dto.StaffResponse;
import com.smartexam.staff.directory.dto.StaffUpdateRequest;
import com.smartexam.staff.directory.entity.Staff;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StaffMapper {

    /*
     * Convert create request into Staff entity.
     *
     * organizationId, id, active, createdAt and updatedAt
     * will be managed by the service/entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizationId", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    Staff toEntity(StaffCreateRequest request);

    /*
     * Update an existing Staff entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizationId", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(
            StaffUpdateRequest request,
            @MappingTarget Staff staff
    );

    /*
     * Convert Staff entity into response DTO.
     */
    @Mapping(
            target = "fullName",
            expression = "java(buildFullName(staff))"
    )
    StaffResponse toResponse(Staff staff);

    /*
     * Build display name.
     */
    default String buildFullName(Staff staff) {

        StringBuilder fullName = new StringBuilder();

        if (staff.getFirstName() != null
                && !staff.getFirstName().isBlank()) {
            fullName.append(staff.getFirstName().trim());
        }

        if (staff.getMiddleName() != null
                && !staff.getMiddleName().isBlank()) {

            if (!fullName.isEmpty()) {
                fullName.append(" ");
            }

            fullName.append(staff.getMiddleName().trim());
        }

        if (staff.getLastName() != null
                && !staff.getLastName().isBlank()) {

            if (!fullName.isEmpty()) {
                fullName.append(" ");
            }

            fullName.append(staff.getLastName().trim());
        }

        return fullName.toString();
    }
}