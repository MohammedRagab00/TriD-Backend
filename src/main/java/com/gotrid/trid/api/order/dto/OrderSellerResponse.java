package com.gotrid.trid.api.order.dto;

import com.gotrid.trid.api.product.dto.ProductVariantResponse;
import com.gotrid.trid.core.order.model.Status;

public record OrderSellerResponse(
        Integer orderId,
        Integer productId,
        ProductVariantResponse variant,
        int quantity,
        Status status
) {
}
