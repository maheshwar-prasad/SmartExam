package com.smartexam.role.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest {

    @NotBlank(message = "Role name is required")
    @Size(max = 50)
    private String name;

    @Size(max = 300)
    private String description;

    private Boolean active;

    private Set<Long> permissionIds;

}