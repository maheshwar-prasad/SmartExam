package com.smartexam.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DashboardSummaryResponse {

    private long totalOrganizations;

    private long activeOrganizations;

    private long totalStudents;

    private long activeStudents;

    private long totalTeachers;

    private long activeTeachers;

    private long totalClasses;

    private long activeClasses;

    private long totalSections;

    private long activeSections;

    private long totalSubjects;

    private long activeSubjects;

    private long totalExams;

    private long activeExams;

    private long scheduledExams;

    private long inProgressExams;

    private long completedExams;
}