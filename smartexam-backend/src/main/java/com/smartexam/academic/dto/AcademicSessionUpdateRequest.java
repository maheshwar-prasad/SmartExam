package com.smartexam.academic.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicSessionUpdateRequest {

    private String name;

    private String code;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean currentSession;

    private String description;

    private Boolean active;

}