package com.smartexam.studentidcard.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentIdCardBulkRequest {

    @NotEmpty
    private List<Long> studentIds;

    @NotNull
    private Long academicSessionId;

    private String templateName;

    private String remarks;
}