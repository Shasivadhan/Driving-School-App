package com.ksvtech.drivingschool.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DashboardFinanceSummary {

    private final BigDecimal totalRevenueAllTime;
    private final BigDecimal totalRevenueThisMonth;
    private final BigDecimal totalOutstandingDue;
}
