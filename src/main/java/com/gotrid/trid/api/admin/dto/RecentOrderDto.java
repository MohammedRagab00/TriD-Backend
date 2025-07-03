package com.gotrid.trid.api.admin.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecentOrderDto(
        Integer orderId,
        String username,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {
}
