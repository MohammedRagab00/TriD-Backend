package com.gotrid.trid.api.review.service;

import com.gotrid.trid.api.review.dto.ReviewRequest;
import com.gotrid.trid.api.review.dto.ReviewResponse;

import java.util.List;

public interface IReviewService {
    List<ReviewResponse> getAllReviews(Integer productId);

    void createReview(ReviewRequest dto, Integer userId);

    void updateReview(Integer id, ReviewRequest dto, Integer userId);

    void deleteReview(Integer id, Integer userId);
}
