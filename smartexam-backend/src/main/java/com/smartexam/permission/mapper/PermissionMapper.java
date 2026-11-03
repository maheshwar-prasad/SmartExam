package com.smartexam.permission.mapper;

import com.smartexam.permission.dto.PermissionRequest;
import com.smartexam.permission.dto.PermissionResponse;
import com.smartexam.permission.dto.PermissionUpdateRequest;
import com.smartexam.permission.entity.Permission;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PermissionMapper {

    /**
     * Request -> Entity
     */
    Permission toEntity(PermissionRequest request);

    /**
     * Entity -> Response
     */
    PermissionResponse toResponse(Permission permission);

    /**
     * Entity List -> Response List
     */
    List<PermissionResponse> toResponseList(List<Permission> permissions);

    /**
     * Update Existing Entity
     */
    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(
            PermissionUpdateRequest request,
            @MappingTarget Permission permission
    );

}