package com.gotrid.trid.core.coupon.mapper;

import com.gotrid.trid.api.coupon.dto.RestrictionDto;
import com.gotrid.trid.core.coupon.model.CouponRestriction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestrictionMapper {
    @Mapping(target = "id", ignore = true)
    CouponRestriction toEntity(RestrictionDto req);
}
