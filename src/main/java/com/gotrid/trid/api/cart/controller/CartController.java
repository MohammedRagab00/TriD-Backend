package com.gotrid.trid.api.cart.controller;

import com.gotrid.trid.api.cart.service.CartService;
import com.gotrid.trid.api.product.dto.ProductVariantResponse;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.config.security.userdetails.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/cart")
@Tag(name = "Cart", description = "Endpoints for managing the user's cart")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('USER')")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Add a product variant to the cart")
    @PostMapping
    public ResponseEntity<Void> addToCart(
            @RequestParam
            @Parameter(description = "ID of the product variant to add or update its quantity", required = true)
            Integer variantId,

            @RequestParam(defaultValue = "1")
            @Parameter(description = "Quantity of the product variant", schema = @Schema(minimum = "1"))
            @Positive
            Integer quantity,

            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        cartService.addToCart(variantId, quantity, principal.user().getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Removes a product variant from the cart")
    @DeleteMapping
    public ResponseEntity<Void> removeFromCart(
            @RequestParam
            @Parameter(description = "ID of the product variant to remove", required = true)
            Integer variantId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        cartService.removeFromCart(variantId, principal.user().getId());
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Retrieve product variants in the user's cart",
            description = "Returns a paginated list of product variants in the user's cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product variants retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    public ResponseEntity<PageResponse<ProductVariantResponse>> getCart(
            @Parameter(description = "Page number", schema = @Schema(defaultValue = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Items per page", schema = @Schema(defaultValue = "10"))
            @RequestParam(defaultValue = "10") int size,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(cartService.getCart(page, size, principal.user().getId()));
    }

}
