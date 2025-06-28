package com.gotrid.trid.api.review.controller;


import com.gotrid.trid.api.review.dto.ReviewRequest;
import com.gotrid.trid.api.review.dto.ReviewResponse;
import com.gotrid.trid.api.review.dto.ReviewUpdate;
import com.gotrid.trid.api.review.dto.ReviewWithUserCheckResponse;
import com.gotrid.trid.api.review.service.IReviewService;
import com.gotrid.trid.api.user.dto.UserProfileResponse;
import com.gotrid.trid.api.user.service.UserService;
import com.gotrid.trid.config.security.userdetails.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Review", description = "Review management API")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/review")
public class ReviewController {

    private final IReviewService reviewService;
    private final UserService userService;

    @Operation(summary = "Get all reviews", description = "Fetch a list of all reviews for a specific product")
    @ApiResponse(responseCode = "200", description = "List of reviews retrieved successfully")
    @GetMapping("all/{productId}")
    public ResponseEntity<?> getAll(
            @PathVariable Integer productId
    ) {
        return (ResponseEntity<?>) reviewService.getAllReviews(productId);
    }

    @Operation(summary = "Get all reviews", description = "Fetch a list of all reviews for a specific product")
    @ApiResponse(responseCode = "200", description = "List of reviews retrieved successfully")
    @GetMapping("/{productId}")
    public ReviewWithUserCheckResponse getAll(
            @PathVariable Integer productId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        List<ReviewResponse> reviews = reviewService.getAllReviews(productId);
        UserProfileResponse username = userService.getUserProfile(principal.user().getEmail());
        boolean hasReviewed = reviewService.existsByProductIdAndUserId(productId, principal.user().getId());

        return new ReviewWithUserCheckResponse(reviews, username, hasReviewed);
    }
    @Operation(summary = "Create a new review", description = "Add a new review for a product")
    @ApiResponse(responseCode = "201", description = "Review created successfully")
    @PostMapping
    public ResponseEntity<Void> create(
            @Parameter(description = "Review details", required = true)
            @RequestBody @Valid ReviewRequest dto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        Integer id = reviewService.createReview(dto, principal.user().getId());
        return ResponseEntity.created(URI.create("api/v1/review/" + id)).build();
    }

    @Operation(summary = "Update an existing review", description = "Modify rating or comment of a review")
    @ApiResponse(responseCode = "200", description = "Review updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Parameter(description = "ID of the review to update", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Updated review details", required = true)
            @RequestBody @Valid ReviewUpdate dto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        reviewService.updateReview(id, dto, principal.user().getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a review", description = "Remove a review by ID")
    @ApiResponse(responseCode = "204", description = "Review deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the review to delete", required = true)
            @PathVariable Integer id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        reviewService.deleteReview(id, principal.user().getId());
        return ResponseEntity.noContent().build();
    }
}

