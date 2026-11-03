package com.smartexam.studentwithdrawal.dto;

import com.smartexam.studentwithdrawal.enums.WithdrawalStatus;

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
public class StudentWithdrawalResponse {

    private Long id;

    private Long studentId;

    private String studentCode;

    private String admissionNo;

    private String studentName;

    private String mobile;

    private String email;

    private Long organizationId;

    private LocalDate withdrawalDate;

    private String reason;

    private String remarks;

    private String lastClassName;

    private String lastAcademicSession;

    private WithdrawalStatus status;

    private Boolean active;

    private String createdAt;

    private String updatedAt;
}