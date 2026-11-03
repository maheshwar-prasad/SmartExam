package com.smartexam.studentidcard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentIdCardGenerateRequest {

    private Long academicSessionId;

    @NotBlank(
            message = "Template name is required"
    )
    private String templateName;

    private String remarks;
}