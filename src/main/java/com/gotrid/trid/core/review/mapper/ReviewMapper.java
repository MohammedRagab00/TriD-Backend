package com.gotrid.trid.core.review.mapper;

import com.gotrid.trid.api.review.dto.ReviewRequest;
import com.gotrid.trid.api.review.dto.ReviewResponse;
import com.gotrid.trid.api.review.dto.ReviewUpdate;
import com.gotrid.trid.core.review.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getId(),
                review.getProduct().getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedDate(),
                review.getLastModifiedDate()
        );
    }

    public Review toEntity(ReviewRequest dto) {
        return Review.builder()
                .rating(dto.rating())
                .comment(dto.comment())
                .build();
    }

    public void updateExisting(ReviewUpdate dto, Review entity) {
        entity.setRating(dto.rating());
        entity.setComment(dto.comment());
    }
}
