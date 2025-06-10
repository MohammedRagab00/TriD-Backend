package com.gotrid.trid.api.coupon.controller;

import com.gotrid.trid.api.coupon.dto.CouponRequest;
import com.gotrid.trid.api.coupon.dto.CouponResponse;
import com.gotrid.trid.api.coupon.dto.RestrictionDto;
import com.gotrid.trid.api.coupon.service.CouponService;
import com.gotrid.trid.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/coupons")
@Tag(name = "Coupons", description = "Coupons management APIs (Admin only)")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<Integer> addCoupon(
            @RequestBody @Valid CouponRequest couponRequest
    ) {
        Integer id = couponService.addCoupon(couponRequest);
        return ResponseEntity.created(URI.create("api/v1/coupons/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCoupon(
            @PathVariable Integer id,
            @RequestBody @Valid CouponRequest couponRequest
    ) {
        couponService.updateCoupon(id, couponRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<CouponResponse>> getCoupons(
            @Parameter(description = "Page number", schema = @Schema(defaultValue = "0"))
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Items per page", schema = @Schema(defaultValue = "10"))
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(couponService.getCoupons(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponse> getCouponById(
            @Parameter(description = "ID of the coupon") @PathVariable Integer id
    ) {
        return ResponseEntity.ok(couponService.getCoupon(id));
    }

    @PostMapping("/restrictions")
    public ResponseEntity<Void> addCouponRestriction(
            @RequestBody @Valid RestrictionDto restrictionDto
    ) {
        couponService.addCouponRestriction(restrictionDto);
        return ResponseEntity.noContent().build();
    }
}
