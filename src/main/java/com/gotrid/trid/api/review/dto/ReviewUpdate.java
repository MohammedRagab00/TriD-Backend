package com.gotrid.trid.api.review.dto;

import jakarta.validation.constraints.NotNull;

public record ReviewUpdate(
        @NotNull(message = "Rating cannot be null")
        Short rating,
        String comment
) {
}
