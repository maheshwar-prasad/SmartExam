package com.smartexam.alumni.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumniRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    private LocalDate leavingDate;

    private Integer graduationYear;

    @Size(max = 100)
    private String lastClassName;

    @Size(max = 100)
    private String lastAcademicSession;

    @Size(max = 255)
    private String higherEducation;

    @Size(max = 255)
    private String currentOrganization;

    @Size(max = 150)
    private String currentDesignation;

    @Size(max = 100)
    private String currentCity;

    @Size(max = 100)
    private String currentCountry;

    @Size(max = 500)
    private String remarks;
}