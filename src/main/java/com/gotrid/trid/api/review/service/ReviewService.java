package com.gotrid.trid.api.review.service;

import com.gotrid.trid.api.review.dto.ReviewDto;
import com.gotrid.trid.core.product.model.Product;
import com.gotrid.trid.core.product.repository.ProductRepository;
import com.gotrid.trid.core.review.model.Review;
import com.gotrid.trid.core.review.repository.ReviewRepository;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.core.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public ReviewDto createReview(ReviewDto dto, Integer userId
    ) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Product product = productRepository.findById(Math.toIntExact(dto.getProductId()))
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        review = reviewRepository.save(review);
        return mapToDto(review);
    }

    public ReviewDto getReviewById(Integer id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        return mapToDto(review);
    }

    public ReviewDto updateReview(Integer id, ReviewDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (dto.getRating() != null) {
            review.setRating(dto.getRating());
        }
        if (dto.getComment() != null) {
            review.setComment(dto.getComment());
        }

        return mapToDto(reviewRepository.save(review));
    }

    public void deleteReview(Integer id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        reviewRepository.delete(review);
    }

    private ReviewDto mapToDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setUserId(review.getUser().getId());
        dto.setProductId(review.getProduct().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        return dto;
    }
}