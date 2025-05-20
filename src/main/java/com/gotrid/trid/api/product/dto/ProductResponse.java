package com.gotrid.trid.api.product.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Integer id,
        String name,
        String description,
        BigDecimal basePrice
) {
}