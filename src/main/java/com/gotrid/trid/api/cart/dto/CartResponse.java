package com.gotrid.trid.api.cart.dto;

import com.gotrid.trid.api.product.dto.ProductVariantResponse;

public record CartResponse(
        Integer productId,
        Integer quantity,
        ProductVariantResponse variantResponse
) {
}
