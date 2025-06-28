package com.gotrid.trid.api.order.controller;

import com.gotrid.trid.api.order.dto.OrderResponse;
import com.gotrid.trid.api.order.dto.OrderSellerResponse;
import com.gotrid.trid.api.order.dto.UpdateStatusRequest;
import com.gotrid.trid.api.order.service.OrderService;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.config.security.userdetails.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/order")
@Tag(name = "Order", description = "Endpoints for managing orders")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Get User Orders", description = "Retrieve a paginated list of orders for a user")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResponse<OrderResponse>> getOrders(
            @Parameter(description = "Page number", schema = @Schema(defaultValue = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Items per page", schema = @Schema(defaultValue = "10"))
            @RequestParam(defaultValue = "10") int size,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(orderService.getOrders(principal.user().getId(), page, size));
    }

    @Operation(summary = "Get Order by ID", description = "Retrieve a specific order by its ID for the authenticated user")
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Integer orderId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(orderService.getOrder(principal.user().getId(), orderId));
    }


    @Operation(summary = "Get Seller Orders", description = "Retrieve a paginated list of orders for a seller")
    @GetMapping("/seller")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<PageResponse<OrderSellerResponse>> getSellerOrders(
            @Parameter(description = "Page number", schema = @Schema(defaultValue = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Items per page", schema = @Schema(defaultValue = "10"))
            @RequestParam(defaultValue = "10") int size,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(orderService.getSellerOrders(principal.user().getId(), page, size));
    }
    @Operation(summary = "Update Order Status", description = "Update the status of an order by a seller")
    @PutMapping("/status")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> updateOrderStatus(
            @RequestBody UpdateStatusRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        orderService.updateOrderStatus(principal.user().getId(), request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cancel Order", description = "Allow the user to cancel an order")
    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Integer orderId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        orderService.cancelOrder(principal.user().getId(), orderId);
        return ResponseEntity.noContent().build();
    }

}
