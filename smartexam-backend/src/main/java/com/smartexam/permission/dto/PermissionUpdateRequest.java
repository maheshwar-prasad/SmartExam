package com.smartexam.permission.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionUpdateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String module;

    @NotBlank
    private String operation;

    private String description;

    private Boolean active;

}