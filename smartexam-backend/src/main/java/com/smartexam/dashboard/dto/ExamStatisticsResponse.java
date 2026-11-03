package com.smartexam.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExamStatisticsResponse {

    private long totalExams;

    private long activeExams;

    private long draftExams;

    private long scheduledExams;

    private long inProgressExams;

    private long completedExams;

    private long cancelledExams;

    private long upcomingExams;
}