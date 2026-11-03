package com.smartexam.gradingsystem.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GradingSystemResponse(
        Long id,
        Long organizationId,
        String name,
        String code,
        String description,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<GradingGradeResponse> grades
) {}