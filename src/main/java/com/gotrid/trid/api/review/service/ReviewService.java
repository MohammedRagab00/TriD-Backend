package com.gotrid.trid.api.review.service;

import com.gotrid.trid.api.review.dto.ReviewRequest;
import com.gotrid.trid.api.review.dto.ReviewResponse;
import com.gotrid.trid.common.exception.custom.UnAuthorizedException;
import com.gotrid.trid.core.product.model.Product;
import com.gotrid.trid.core.product.repository.ProductRepository;
import com.gotrid.trid.core.review.mapper.ReviewMapper;
import com.gotrid.trid.core.review.model.Review;
import com.gotrid.trid.core.review.repository.ReviewRepository;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.core.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository usersRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public List<ReviewResponse> getAllReviews(Integer productId) {
        return reviewRepository.findByProduct_Id(productId)
                .stream().map(reviewMapper::toResponse).toList();
    }

    @Override
    public void createReview(ReviewRequest dto, Integer userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Review review = reviewMapper.toEntity(dto);
        review.setUser(user);
        review.setProduct(product);

        reviewRepository.save(review);
    }

    @Override
    public void updateReview(Integer id, ReviewRequest dto, Integer userId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        isUserAuthorized(review, userId);

        reviewMapper.updateExisting(dto, review);

        reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Integer id, Integer userId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        isUserAuthorized(review, userId);
        reviewRepository.delete(review);
    }

    private void isUserAuthorized(Review review, Integer userId) {
        if (!review.getUser().getId().equals(userId)) {
            throw new UnAuthorizedException("You are not authorized to modify this review");
        }
    }
}