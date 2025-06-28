package com.gotrid.trid.api.review.dto;

import com.gotrid.trid.api.user.dto.UserProfileResponse;

import java.util.List;

public record ReviewWithUserCheckResponse(
        List<ReviewResponse> reviews,
        UserProfileResponse username,
        boolean hasReviewed
) {
}

