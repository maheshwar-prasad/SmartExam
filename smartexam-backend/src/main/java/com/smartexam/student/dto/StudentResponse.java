package com.smartexam.student.dto;

import com.smartexam.common.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {

    private Long id;

    private String studentCode;

    private String admissionNo;

    private String rollNo;

    private LocalDate admissionDate;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String bloodGroup;

    private String fatherName;

    private String motherName;

    private String guardianName;

    private String mobile;

    private String email;

    private String address;

    private String photoUrl;

    // Organization
    private Long organizationId;

    private String organizationName;

    // Class
    private Long classId;

    private String className;

    // Section
    private Long sectionId;

    private String sectionName;

    private Boolean active;

    private LocalDateTime createdAt;
}