package com.smartexam.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ResultStatisticsResponse {

    private long totalResults;

    private long activeResults;

    private long passedResults;

    private long failedResults;

    private long pendingResults;

    private double passPercentage;

    private BigDecimal averagePercentage;

    private BigDecimal highestPercentage;

    private BigDecimal lowestPercentage;
}