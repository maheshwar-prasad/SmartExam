package com.smartexam.timetable.dto;

import com.smartexam.common.enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableSearchRequest {

    private String keyword;

    private Long teacherSubjectAssignmentId;

    private Long teacherId;

    private Long subjectId;

    private Long academicSessionId;

    private Long classRoomId;

    private Long sectionId;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean active;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String sortBy = "dayOfWeek";

    @Builder.Default
    private String direction = "ASC";
}