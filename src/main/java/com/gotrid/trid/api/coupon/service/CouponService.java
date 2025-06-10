package com.gotrid.trid.api.coupon.service;

import com.gotrid.trid.api.coupon.dto.CouponRequest;
import com.gotrid.trid.api.coupon.dto.CouponResponse;
import com.gotrid.trid.api.coupon.dto.RestrictionDto;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.core.coupon.mapper.CouponMapper;
import com.gotrid.trid.core.coupon.mapper.RestrictionMapper;
import com.gotrid.trid.core.coupon.model.Coupon;
import com.gotrid.trid.core.coupon.model.CouponRestriction;
import com.gotrid.trid.core.coupon.model.Discount;
import com.gotrid.trid.core.coupon.repository.CouponRepository;
import com.gotrid.trid.core.coupon.repository.RestrictionRepository;
import com.gotrid.trid.core.product.repository.ProductRepository;
import com.gotrid.trid.core.shop.repository.ShopRepository;
import com.gotrid.trid.core.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.math.BigDecimal.ONE;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final RestrictionRepository restrictionRepository;
    private final CouponMapper couponMapper;
    private final RestrictionMapper restrictionMapper;

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    public Integer addCoupon(CouponRequest request) {
        couponRepository.findByCode(request.code())
                .ifPresent(coupon -> {
                    throw new IllegalArgumentException("Coupon with code " + request.code() + " already exists.");
                });

        validate(request);

        return couponRepository.saveAndFlush(couponMapper.toEntity(request)).getId();
    }

    public void updateCoupon(Integer id, CouponRequest request) {
        Coupon coupon = findCouponById(id);
        if (!coupon.getCode().equals(request.code()))
            couponRepository.findByCode(request.code())
                    .ifPresent(coupon1 -> {
                        throw new IllegalArgumentException("Coupon with code " + request.code() + " already exists.");
                    });

        validate(request);

        couponMapper.updateEntityFromRequest(request, coupon);

        couponRepository.save(coupon);
    }

    public void addCouponRestriction(RestrictionDto request) {
        Coupon coupon = findCouponById(request.id());
        if (restrictionRepository.existsByCouponAndRestrictionValueAndRestrictionType(coupon, request.restrictionValue(), request.restrictionType()))
            throw new IllegalArgumentException("Restriction with value " + request.restrictionValue() + " and type " + request.restrictionType() + " already exists for coupon with id " + request.id());

        switch (request.restrictionType()) {
            case CUSTOMER -> {
                if (!userRepository.existsById(request.restrictionValue()))
                    throw new EntityNotFoundException("Customer with id " + request.restrictionValue() + " not found");
            }
            case PRODUCT -> {
                if (!productRepository.existsById(request.restrictionValue()))
                    throw new EntityNotFoundException("Product with id " + request.restrictionValue() + " not found");
            }
            case STORE -> {
                if (!shopRepository.existsById(request.restrictionValue()))
                    throw new EntityNotFoundException("Shop with id " + request.restrictionValue() + " not found");
            }
            case CATEGORY -> {
                //todo: Implement category restriction logic
            }
            default -> throw new IllegalArgumentException("Unsupported restriction type: " + request.restrictionType());
        }
        CouponRestriction restriction = restrictionMapper.toEntity(request);
        restriction.setCoupon(coupon);
        restrictionRepository.save(restriction);
    }

    @Transactional(readOnly = true)
    public PageResponse<CouponResponse> getCoupons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Coupon> couponsPage = couponRepository.findAll(pageable);

        List<CouponResponse> responses = couponsPage.stream()
                .map(couponMapper::toResponse)
                .toList();

        return new PageResponse<>(
                responses,
                couponsPage.getNumber(),
                couponsPage.getSize(),
                couponsPage.getTotalElements(),
                couponsPage.getTotalPages(),
                couponsPage.isFirst(),
                couponsPage.isLast()
        );
    }

    @Transactional(readOnly = true)
    public CouponResponse getCoupon(Integer id) {
        Coupon coupon = findCouponById(id);
        return couponMapper.toResponse(coupon);
    }

    private void validate(CouponRequest request) {
        Discount discount;
        try {
            discount = Discount.valueOf(request.discount_type().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid discount type: " + request.discount_type() + ". Must be one of: FIXED_AMOUNT, PERCENTAGE");
        }

        if (discount == Discount.PERCENTAGE
                && request.discount_value().compareTo(ONE) > 0) {
            throw new IllegalArgumentException("Percentage discount cannot exceed 100%, must be between 0 and 1");
        }

        if (!request.valid_from().isBefore(request.valid_until())) {
            throw new IllegalArgumentException("Valid from date must be before valid until date");
        }
    }

    private Coupon findCouponById(Integer id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coupon with id " + id + " not found"));
    }
}
