package com.smartexam.gradingsystem.dto;

import java.math.BigDecimal;

public record GradingGradeResponse(
        Long id,
        String grade,
        BigDecimal minimumPercentage,
        BigDecimal maximumPercentage,
        BigDecimal gradePoint,
        String remarks,
        Boolean passing,
        Integer displayOrder
) {}