package com.gotrid.trid.api.product.dto;

import java.math.BigDecimal;

public record ProductVariantResponse(
        Integer id,
        String color,
        String size,
        int stock,
        BigDecimal price
) {}