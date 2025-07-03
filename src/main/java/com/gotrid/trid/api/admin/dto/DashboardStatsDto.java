package com.gotrid.trid.api.admin.dto;


import java.math.BigDecimal;

public record DashboardStatsDto(
        long totalNumOfUsers,
        long totalNumOfOrders,
        BigDecimal totalRevenue,
        BigDecimal netProfit
) {
}
