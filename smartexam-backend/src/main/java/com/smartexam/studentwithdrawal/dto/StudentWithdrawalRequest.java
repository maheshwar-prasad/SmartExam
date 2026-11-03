package com.smartexam.studentwithdrawal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentWithdrawalRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Withdrawal date is required")
    private LocalDate withdrawalDate;

    @NotBlank(message = "Withdrawal reason is required")
    @Size(max = 255)
    private String reason;

    @Size(max = 500)
    private String remarks;

    @Size(max = 100)
    private String lastClassName;

    @Size(max = 100)
    private String lastAcademicSession;
}