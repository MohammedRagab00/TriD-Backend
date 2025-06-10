package com.gotrid.trid.api.coupon.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponRequest(
        @NotNull(message = "Coupon code must not be null")
        @Size(min = 6, message = "Coupon code must be at least 6 characters long")
        @Pattern(regexp = "\\S+", message = "Coupon code must not contain spaces")
        String code,
        @NotBlank(message = "Discount type must not be blank")
        String discount_type,
        @Positive(message = "Discount value must be positive")
        BigDecimal discount_value,
        @PositiveOrZero(message = "Minimum spend must be zero or positive")
        BigDecimal min_spend,
        @Positive(message = "Maximum discount must be positive")
        BigDecimal max_discount,
        @Positive(message = "Usage limit must be positive")
        int usage_limit,
        @Future(message = "Valid from date must be in the future")
        LocalDateTime valid_from,
        @Future(message = "Valid until date must be in the future")
        LocalDateTime valid_until
) {
}
