package com.gotrid.trid.api.shop.dto.product;

import java.math.BigDecimal;

public record ProductResponse(
        Integer id,
        String name,
        String sizes,
        String colors,
        String description,
        BigDecimal basePrice
) {
}