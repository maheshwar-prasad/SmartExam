package com.smartexam.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DashboardFilterRequest {

    private Long organizationId;

    private Long academicSessionId;

    private Long classRoomId;

    private Long sectionId;

    private LocalDate fromDate;

    private LocalDate toDate;
}