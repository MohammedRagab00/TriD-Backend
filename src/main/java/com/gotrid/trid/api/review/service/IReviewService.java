package com.gotrid.trid.api.review.service;

import com.gotrid.trid.api.review.dto.ReviewRequest;
import com.gotrid.trid.api.review.dto.ReviewResponse;
import com.gotrid.trid.api.review.dto.ReviewUpdate;

import java.util.List;

public interface IReviewService {
    List<ReviewResponse> getAllReviews(Integer productId);
    Boolean checkReviewWithUsers(Integer productId, Integer userId);
    Integer createReview(ReviewRequest dto, Integer userId);

    void updateReview(Integer id, ReviewUpdate dto, Integer userId);

    void deleteReview(Integer id, Integer userId);
    boolean existsByProductIdAndUserId(Integer productId, Integer userId);

}
