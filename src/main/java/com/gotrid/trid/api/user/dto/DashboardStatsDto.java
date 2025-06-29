package com.gotrid.trid.api.user.dto;


import java.math.BigDecimal;

public record DashboardStatsDto(
        int totalUsers,
        int totalOrders,
        BigDecimal totalRevenue,
        BigDecimal netProfit
) {}

