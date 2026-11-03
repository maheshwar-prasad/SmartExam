package com.smartexam.teacher.dto;

import com.smartexam.common.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    @Size(max = 200)
    private String qualification;

    @Min(
            value = 0,
            message = "Experience cannot be negative"
    )
    private Integer experienceYears;

    @Size(max = 200)
    private String specialization;

    @Email(message = "Invalid email")
    @Size(max = 150)
    private String email;

    @Pattern(
            regexp = "^$|^[0-9]{10}$",
            message = "Mobile number must contain exactly 10 digits"
    )
    private String mobile;

    @Size(max = 500)
    private String address;

    @NotNull(message = "Organization is required")
    private Long organizationId;

    @NotNull(message = "User is required")
    private Long userId;
}