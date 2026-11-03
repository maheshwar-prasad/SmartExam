package com.smartexam.user.dto;

import com.smartexam.common.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank(message = "Mobile is required")
    @Pattern(regexp = "^[0-9]{10}$",
            message = "Mobile number must contain exactly 10 digits")
    private String mobile;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Organization is required")
    private Long organizationId;

    @NotNull(message = "Role is required")
    private Long roleId;

}