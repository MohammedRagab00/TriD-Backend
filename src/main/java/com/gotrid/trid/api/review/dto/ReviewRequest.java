package com.gotrid.trid.api.review.dto;

import jakarta.validation.constraints.NotNull;

public record ReviewRequest(
        @NotNull(message = "Product ID cannot be null")
        Integer productId,
        @NotNull(message = "Rating cannot be null")
        Short rating,
        String comment
) {
}
