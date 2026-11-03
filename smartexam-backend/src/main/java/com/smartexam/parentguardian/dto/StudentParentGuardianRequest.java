package com.smartexam.parentguardian.dto;

import com.smartexam.parentguardian.enums.ParentGuardianRelation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentParentGuardianRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Parent/Guardian ID is required")
    private Long parentGuardianId;

    @NotNull(message = "Relation is required")
    private ParentGuardianRelation relation;

    @Builder.Default
    private Boolean primaryContact = false;

    @Builder.Default
    private Boolean emergencyContact = false;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;
}