package com.smartexam.subject.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectUpdateRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 30)
    private String code;

    @Size(max = 500)
    private String description;

    @Min(value = 0)
    @Max(value = 1000)
    private Integer theoryMarks;

    @Min(value = 0)
    @Max(value = 1000)
    private Integer practicalMarks;

    @Min(value = 0)
    private Integer passMarks;

    @Min(value = 1)
    private Integer displayOrder;

    private Boolean active;

    private Long organizationId;

    private Long academicSessionId;

}