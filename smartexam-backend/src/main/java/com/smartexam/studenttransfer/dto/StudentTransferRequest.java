package com.smartexam.studenttransfer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentTransferRequest {

    @NotNull(message = "Student is required")
    private Long studentId;

    @NotNull(message = "Source Academic Session is required")
    private Long sourceAcademicSessionId;

    @NotNull(message = "Source Class Room is required")
    private Long sourceClassRoomId;

    private Long sourceSectionId;

    @NotNull(message = "Target Academic Session is required")
    private Long targetAcademicSessionId;

    @NotNull(message = "Target Class Room is required")
    private Long targetClassRoomId;

    private Long targetSectionId;

    @NotNull(message = "Transfer Date is required")
    private LocalDate transferDate;

    private String reason;

    private String remarks;
}