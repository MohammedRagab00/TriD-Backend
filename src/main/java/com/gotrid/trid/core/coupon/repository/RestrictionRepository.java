package com.gotrid.trid.core.coupon.repository;

import com.gotrid.trid.core.coupon.model.Coupon;
import com.gotrid.trid.core.coupon.model.CouponRestriction;
import com.gotrid.trid.core.coupon.model.Restriction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestrictionRepository extends JpaRepository<CouponRestriction, Integer> {
    boolean existsByCouponAndRestrictionValueAndRestrictionType(Coupon coupon, Integer restrictionValue, Restriction restrictionType);
}
