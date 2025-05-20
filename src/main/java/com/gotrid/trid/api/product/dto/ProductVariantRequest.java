package com.gotrid.trid.api.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductVariantRequest(
        @NotBlank(message = "Color is required")
        @Size(min = 3, max = 50, message = "length of color must be between 3 and 50 characters")
        String color,

        @NotBlank(message = "Size is required")
        @Size(min = 1, max = 20, message = "length of size must be between 1 and 20 characters")
        String size,

        @PositiveOrZero(message = "Stock must be greater or equal to zero")
        int stock,

        @Positive(message = "Price must be greater then zero")
        BigDecimal price
) {
}