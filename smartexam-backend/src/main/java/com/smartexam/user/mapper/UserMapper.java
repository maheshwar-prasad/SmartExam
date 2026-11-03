package com.smartexam.user.mapper;

import com.smartexam.organization.entity.Organization;
import com.smartexam.role.entity.Role;
import com.smartexam.user.dto.UserRequest;
import com.smartexam.user.dto.UserResponse;
import com.smartexam.user.dto.UserUpdateRequest;
import com.smartexam.user.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    /**
     * Request -> Entity
     */
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequest request);

    /**
     * Entity -> Response
     */
    @Mapping(source = "organization.name", target = "organizationName")
    @Mapping(source = "role.name", target = "roleName")
    UserResponse toResponse(User user);

    /**
     * Entity List -> DTO List
     */
    List<UserResponse> toResponseList(List<User> users);

    /**
     * Update Existing User
     */
    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntity(
            UserUpdateRequest request,
            @MappingTarget User user
    );

}