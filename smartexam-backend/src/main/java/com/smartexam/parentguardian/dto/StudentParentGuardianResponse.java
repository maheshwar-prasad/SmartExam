package com.smartexam.parentguardian.dto;

import com.smartexam.parentguardian.enums.ParentGuardianRelation;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentParentGuardianResponse {

    private Long id;

    private Long studentId;

    private String studentName;

    private Long parentGuardianId;

    private String parentGuardianName;

    private ParentGuardianRelation relation;

    private String mobile;

    private String email;

    private Boolean primaryContact;

    private Boolean emergencyContact;

    private String remarks;

    private Boolean active;
}