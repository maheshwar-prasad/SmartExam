package com.smartexam.student.dto;

import com.smartexam.common.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateRequest {

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

    @Pattern(regexp = "^[0-9]{10}$")
    private String mobile;

    @Email
    private String email;

    private String address;

    private String photoUrl;

    private Boolean active;

}