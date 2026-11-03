package com.smartexam.permission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionSearchRequest {

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String sortBy = "name";

    @Builder.Default
    private String direction = "ASC";

    /**
     * Search by name, code or description
     */
    private String keyword;

    /**
     * USER, ROLE, STUDENT, EXAM...
     */
    private String module;

    /**
     * CREATE, UPDATE, DELETE, VIEW...
     */
    private String operation;

    /**
     * Active / Inactive
     */
    private Boolean active;

}