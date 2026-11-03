package com.smartexam.academic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicSessionRequest {

    @NotBlank(message = "Session name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Session code is required")
    @Size(max = 30)
    private String code;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private Boolean currentSession;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Organization is required")
    private Long organizationId;

}