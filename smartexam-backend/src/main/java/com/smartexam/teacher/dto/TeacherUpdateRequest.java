package com.smartexam.teacher.dto;

import com.smartexam.common.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherUpdateRequest {

    @Size(max = 30)
    private String employeeCode;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    private Gender gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private LocalDate joiningDate;

    @Size(max = 200)
    private String qualification;

    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experienceYears;

    @Size(max = 200)
    private String specialization;

    @Email(message = "Invalid email")
    @Size(max = 150)
    private String email;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must contain exactly 10 digits")
    private String mobile;

    @Size(max = 500)
    private String address;

    @Size(max = 500)
    private String photoUrl;

    private Boolean active;

    private Long organizationId;

    private Long userId;

}