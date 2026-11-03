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
public class TimetableResponse {

    private Long id;

    private Long teacherSubjectAssignmentId;

    private Long teacherId;

    private String teacherCode;

    private String teacherName;

    private Long subjectId;

    private String subjectCode;

    private String subjectName;

    private Long academicSessionId;

    private String academicSessionName;

    private Long classRoomId;

    private String classRoomCode;

    private String classRoomName;

    private Long sectionId;

    private String sectionCode;

    private String sectionName;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private String roomNumber;

    private Boolean active;
}