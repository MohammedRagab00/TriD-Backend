package com.gotrid.trid.api.order.dto;

public record OrderItemResponse(
        Integer productId,
        Integer variantId
) {
}