package com.smartexam.role.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {

    @NotBlank(message = "Role name is required")
    @Size(max = 50)
    private String name;

    @Size(max = 300)
    private String description;

    /**
     * Permission IDs assigned to this role.
     */
    @NotEmpty(message = "At least one permission is required")
    private Set<Long> permissionIds;

}