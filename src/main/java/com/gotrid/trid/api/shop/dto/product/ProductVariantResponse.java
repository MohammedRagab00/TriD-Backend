package com.gotrid.trid.api.shop.dto.product;

import java.math.BigDecimal;

public record ProductVariantResponse(
        Integer id,
        String color,
        String size,
        int stock,
        BigDecimal price
) {}