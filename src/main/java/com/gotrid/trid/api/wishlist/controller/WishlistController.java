package com.gotrid.trid.api.wishlist.controller;

import com.gotrid.trid.api.product.dto.ProductResponse;
import com.gotrid.trid.api.wishlist.service.WishlistService;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.config.security.userdetails.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/wishlist")
@Tag(name = "Wishlist", description = "Endpoints for managing the user's wishlist")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('USER')")
public class WishlistController {
    private final WishlistService wishlistService;

    @Operation(
            summary = "Add or remove a product from the wishlist",
            description = "Toggles the presence of a product in the user's wishlist. If the product is already in the wishlist, it will be removed; otherwise, it will be added."
    )
    @PutMapping
    public ResponseEntity<Void> addToWishlist(
            @RequestParam
            @Parameter(description = "ID of the product to add or remove", required = true)
            Integer productId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        wishlistService.addOrRemoveFromWishlist(productId, principal.user().getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get wishlist products", description = "Retrieves all products in the user's wishlist")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getWishlist(
            @Parameter(description = "Page number", schema = @Schema(defaultValue = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Items per page", schema = @Schema(defaultValue = "10"))
            @RequestParam(defaultValue = "10") int size,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(wishlistService.getWishlist(page, size, principal.user().getId()));
    }
}
