package com.smartexam.role.mapper;

import com.smartexam.permission.entity.Permission;
import com.smartexam.role.dto.RoleRequest;
import com.smartexam.role.dto.RoleResponse;
import com.smartexam.role.dto.RoleUpdateRequest;
import com.smartexam.role.entity.Role;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleMapper {

    /**
     * Request -> Entity
     */
    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleRequest request);

    /**
     * Entity -> Response
     */
    @Mapping(target = "permissions",
            expression = "java(getPermissionNames(role.getPermissions()))")
    RoleResponse toResponse(Role role);

    /**
     * Entity List -> Response List
     */
    List<RoleResponse> toResponseList(List<Role> roles);

    /**
     * Update Existing Entity
     */
    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "permissions", ignore = true)
    void updateEntity(
            RoleUpdateRequest request,
            @MappingTarget Role role
    );

    /**
     * Convert Permission Entities -> Permission Names
     */
    default Set<String> getPermissionNames(Set<Permission> permissions) {

        if (permissions == null) {
            return Set.of();
        }

        return permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

    }

}