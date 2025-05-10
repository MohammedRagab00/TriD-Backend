package com.gotrid.trid.api.shop.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Product name is required")
        @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
        String name,

        @NotBlank(message = "Product sizes are required")
        @Size(min = 3, max = 50, message = "Product sizes must be between 3 and 50 characters")
        String sizes,

        @NotBlank(message = "Product colors are required")
        @Size(min = 3, max = 50, message = "Product colors must be between 3 and 50 characters")
        String colors,

        @NotBlank(message = "Product description is required")
        @Size(min = 10, max = 1000, message = "Product description must be between 10 and 1000 characters")
        String description,

        @Positive(message = "Base price must be greater then zero")
        BigDecimal basePrice
) {
}
