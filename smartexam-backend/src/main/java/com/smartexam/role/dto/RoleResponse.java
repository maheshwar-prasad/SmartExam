package com.smartexam.role.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private Long id;

    private String name;

    private String description;

    private Boolean active;

    /**
     * Permission names assigned to this role.
     */
    private Set<String> permissions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}