
package com.smartexam.institution.department.dto;

import java.time.LocalDateTime;

public record DepartmentResponse(

        Long id,

        Long organizationId,

        Long campusId,

        String name,

        String code,

        String description,

        Boolean active,

        LocalDateTime createdAt,

        String createdBy,

        LocalDateTime updatedAt,

        String updatedBy

) {
}