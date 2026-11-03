package com.smartexam.subject.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequest {

    @NotBlank(message = "Subject name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Subject code is required")
    @Size(max = 30)
    private String code;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Theory marks are required")
    @Min(value = 0)
    @Max(value = 1000)
    private Integer theoryMarks;

    @NotNull(message = "Practical marks are required")
    @Min(value = 0)
    @Max(value = 1000)
    private Integer practicalMarks;

    @NotNull(message = "Pass marks are required")
    @Min(value = 0)
    private Integer passMarks;

    @NotNull(message = "Display order is required")
    @Min(value = 1)
    private Integer displayOrder;

    @NotNull(message = "Organization is required")
    private Long organizationId;

    @NotNull(message = "Academic Session is required")
    private Long academicSessionId;

}