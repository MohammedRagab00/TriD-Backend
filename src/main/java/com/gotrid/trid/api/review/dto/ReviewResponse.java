package com.gotrid.trid.api.review.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Integer id,
        Integer userId,
        Integer productId,
        Short rating,
        String comment,
        LocalDateTime reviewedAt,
        LocalDateTime lastModifiedAt
) {
}