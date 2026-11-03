package com.smartexam.alumni.dto;

import com.smartexam.alumni.enums.AlumniStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumniResponse {

    private Long id;

    private Long studentId;

    private String studentCode;

    private String admissionNo;

    private String studentName;

    private String mobile;

    private String email;

    private Long organizationId;

    private LocalDate leavingDate;

    private Integer graduationYear;

    private String lastClassName;

    private String lastAcademicSession;

    private String higherEducation;

    private String currentOrganization;

    private String currentDesignation;

    private String currentCity;

    private String currentCountry;

    private String remarks;

    private AlumniStatus status;

    private Boolean active;

    private String createdAt;

    private String updatedAt;
}