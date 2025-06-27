package com.gotrid.trid.api.order.dto;

import com.gotrid.trid.core.order.model.Status;

import java.math.BigDecimal;
import java.util.Set;

public record OrderResponse(
        Integer orderId,
        BigDecimal total_amount,
        Status status,
        Set<OrderItemResponse> orderItems
) {
}
