package com.smartexam.teacher.dto;

import com.smartexam.common.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponse {

    private Long id;

    private String employeeCode;

    private String firstName;

    private String lastName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private LocalDate joiningDate;

    private String qualification;

    private Integer experienceYears;

    private String specialization;

    private String email;

    private String mobile;

    private String address;

    private String photoUrl;

    private Boolean active;

    private Long organizationId;

    private String organizationName;

    private Long userId;

    private String userName;

    private String userEmail;

}