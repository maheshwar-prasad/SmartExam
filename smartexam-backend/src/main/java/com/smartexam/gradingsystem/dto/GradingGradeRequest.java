package com.smartexam.gradingsystem.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record GradingGradeRequest(
        @NotBlank
        @Size(max = 20)
        String grade,

        @NotNull
        @DecimalMin("0.00")
        @DecimalMax("100.00")
        BigDecimal minimumPercentage,

        @NotNull
        @DecimalMin("0.00")
        @DecimalMax("100.00")
        BigDecimal maximumPercentage,

        @DecimalMin("0.00")
        BigDecimal gradePoint,

        @Size(max = 300)
        String remarks,

        @NotNull
        Boolean passing,

        @NotNull
        Integer displayOrder
) {}