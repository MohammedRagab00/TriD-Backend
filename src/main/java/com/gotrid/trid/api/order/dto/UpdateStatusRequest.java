package com.gotrid.trid.api.order.dto;

import com.gotrid.trid.core.order.model.Status;

public record UpdateStatusRequest(
        Integer orderId,
        Status newStatus
) {
}