package com.smartexam.exam.dto;

import com.smartexam.common.enums.ExamStatus;
import com.smartexam.common.enums.ExamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequest {

    @NotNull(message = "Organization ID is required.")
    private Long organizationId;

    @NotNull(message = "Academic Session ID is required.")
    private Long academicSessionId;

    @NotNull(message = "Class Room ID is required.")
    private Long classRoomId;

    @NotNull(message = "Section ID is required.")
    private Long sectionId;

    @NotBlank(message = "Exam name is required.")
    @Size(
            max = 150,
            message = "Exam name must not exceed 150 characters."
    )
    private String name;

    @NotNull(message = "Exam type is required.")
    private ExamType examType;

    @NotNull(message = "Start date is required.")
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    private LocalDate endDate;

    private ExamStatus status;

    @Size(
            max = 1000,
            message = "Remarks must not exceed 1000 characters."
    )
    private String remarks;

}