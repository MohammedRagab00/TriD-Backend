package com.gotrid.trid.api.review.controller;



import com.gotrid.trid.api.review.dto.ReviewDto;
import com.gotrid.trid.api.review.service.ReviewService;
import com.gotrid.trid.config.security.userdetails.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Review API", description = "Operations related to product reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Get all reviews", description = "Fetch a list of all reviews")
    @ApiResponse(responseCode = "200", description = "List of reviews retrieved successfully")
    @GetMapping
    public List<ReviewDto> getAll() {
        return reviewService.getAllReviews();
    }

    @Operation(summary = "Get review by ID", description = "Fetch a specific review by its ID")
    @ApiResponse(responseCode = "200", description = "Review found")
    @ApiResponse(responseCode = "404", description = "Review not found")
    @GetMapping("/{id}")
    public ReviewDto getById(
            @Parameter(description = "ID of the review", required = true)
            @PathVariable Integer id) {
        return reviewService.getReviewById(id);
    }

    @Operation(summary = "Create a new review", description = "Add a new review for a product")
    @ApiResponse(responseCode = "201", description = "Review created successfully")
    @PostMapping
    public ReviewDto create(
            @Parameter(description = "Review details", required = true)
            @RequestBody ReviewDto dto,@AuthenticationPrincipal UserPrincipal principal) {
        return reviewService.createReview(dto,principal.user().getId());
    }

    @Operation(summary = "Update an existing review", description = "Modify rating or comment of a review")
    @ApiResponse(responseCode = "200", description = "Review updated successfully")
    @PutMapping("/{id}")
    public ReviewDto update(
            @Parameter(description = "ID of the review to update", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Updated review details", required = true)
            @RequestBody ReviewDto dto) {
        return reviewService.updateReview(id, dto);
    }

    @Operation(summary = "Delete a review", description = "Remove a review by ID")
    @ApiResponse(responseCode = "204", description = "Review deleted successfully")
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "ID of the review to delete", required = true)
            @PathVariable Integer id) {
        reviewService.deleteReview(id);
    }
}

