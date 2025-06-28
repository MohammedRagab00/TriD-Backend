package com.gotrid.trid.api.review.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Integer id,
        String name,
        Integer productId,
        Short rating,
        String comment,
        LocalDateTime reviewedAt,
        LocalDateTime lastModifiedAt,
        boolean canEdit
) {
}