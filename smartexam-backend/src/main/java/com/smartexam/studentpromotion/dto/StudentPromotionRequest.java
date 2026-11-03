package com.smartexam.studentpromotion.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentPromotionRequest {

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

    @NotEmpty(message = "At least one student must be selected")
    private List<Long> studentIds;
}