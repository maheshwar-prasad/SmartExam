package com.smartexam.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class StudentStatisticsResponse {

    private long totalStudents;

    private long activeStudents;

    private long inactiveStudents;

    private List<ClassStudentCount> studentsByClass;

    private List<SectionStudentCount> studentsBySection;

    @Getter
    @Setter
    @Builder
    public static class ClassStudentCount {

        private Long classRoomId;

        private String className;

        private long studentCount;
    }

    @Getter
    @Setter
    @Builder
    public static class SectionStudentCount {

        private Long sectionId;

        private String sectionName;

        private long studentCount;
    }
}