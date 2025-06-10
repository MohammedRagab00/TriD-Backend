package com.gotrid.trid.api.coupon.dto;

import com.gotrid.trid.core.coupon.model.Discount;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record CouponResponse(
        Integer id,
        String code,
        Discount discount_type,
        BigDecimal discount_value,
        BigDecimal min_spend,
        BigDecimal max_discount,
        int usage_limit,
        LocalDateTime valid_from,
        LocalDateTime valid_until,
        Set<RestrictionDto> restrictions
) {
}
