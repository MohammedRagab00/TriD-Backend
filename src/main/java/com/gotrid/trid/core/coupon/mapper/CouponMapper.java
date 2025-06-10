package com.gotrid.trid.core.coupon.mapper;

import com.gotrid.trid.api.coupon.dto.CouponRequest;
import com.gotrid.trid.api.coupon.dto.CouponResponse;
import com.gotrid.trid.api.coupon.dto.RestrictionDto;
import com.gotrid.trid.core.coupon.model.Coupon;
import com.gotrid.trid.core.coupon.model.Discount;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CouponMapper {

    public Coupon toEntity(CouponRequest req) {
        return Coupon.builder()
                .code(req.code())
                .discount_type(Discount.valueOf(req.discount_type().toUpperCase()))
                .discount_value(req.discount_value())
                .min_spend(req.min_spend())
                .max_discount(req.max_discount())
                .usage_limit(req.usage_limit())
                .valid_from(req.valid_from())
                .valid_until(req.valid_until())
                .build();
    }

    public void updateEntityFromRequest(CouponRequest req, Coupon entity) {
        entity.setCode(req.code());
        entity.setDiscount_type(Discount.valueOf(req.discount_type().toUpperCase()));
        entity.setDiscount_value(req.discount_value());
        entity.setMin_spend(req.min_spend());
        entity.setMax_discount(req.max_discount());
        entity.setUsage_limit(req.usage_limit());
        entity.setValid_from(req.valid_from());
        entity.setValid_until(req.valid_until());
    }

    public CouponResponse toResponse(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode(),
                coupon.getDiscount_type(),
                coupon.getDiscount_value(),
                coupon.getMin_spend(),
                coupon.getMax_discount(),
                coupon.getUsage_limit(),
                coupon.getValid_from(),
                coupon.getValid_until(),
                coupon.getRestrictions().stream()
                        .map(restriction -> new RestrictionDto(
                                restriction.getId(),
                                restriction.getRestrictionType(),
                                restriction.getRestrictionValue()))
                        .collect(Collectors.toSet())
        );
    }
}
