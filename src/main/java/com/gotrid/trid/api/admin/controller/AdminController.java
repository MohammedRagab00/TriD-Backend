package com.gotrid.trid.api.admin.controller;

import com.gotrid.trid.api.admin.dto.UserSearchResponse;
import com.gotrid.trid.api.admin.service.IAdminService;
import com.gotrid.trid.api.admin.dto.DashboardStatsDto;
import com.gotrid.trid.api.admin.dto.RecentOrderDto;
import com.gotrid.trid.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Operations", description = "Endpoints for admin-only operations")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final IAdminService adminService;

    @Operation(summary = "Search users by email", description = "Search for users whose email contains the given pattern")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
            @ApiResponse(responseCode = "400", description = "Invalid email pattern")
    })
    @GetMapping("/users/search")
    public ResponseEntity<PageResponse<UserSearchResponse>> searchUsersByEmail(
            @RequestParam
            @Size(min = 3, message = "Search term must be at least 3 characters")
            String email,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "4", required = false) int size
    ) {
        return ResponseEntity.ok(adminService.searchUserByEmail(email, page, size));
    }

    @Operation(summary = "Update user roles", description = "Update roles for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Roles updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping("/users/{id}/roles")
    public ResponseEntity<UserSearchResponse> updateUserRoles(
            @PathVariable Integer id,
            @RequestBody Set<String> roles
    ) {
        return ResponseEntity.ok(adminService.updateUserRoles(id, roles));
    }


    @Operation(
            summary = "Get dashboard statistics",
            description = "Returns total users, orders, revenue, and net profit"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics")
    @GetMapping("/dashboard/stats")
    public DashboardStatsDto getStats() {
        return adminService.getStats();
    }

    @Operation(summary = "Get recent orders", description = "Returns the latest 10 orders")
    @GetMapping("/dashboard/recent-orders")
    public List<RecentOrderDto> getRecentOrders() {
        return adminService.getRecentOrders();
    }
}
