package com.gotrid.trid.core.coupon.model;


import com.gotrid.trid.common.model.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"restrictions"})
@Entity
@Table(name = "coupons")
public class Coupon extends AuditableEntity {
    @Column(nullable = false, unique = true, length = 40)
    private String code;
    @Column(nullable = false)
    private Discount discount_type;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discount_value;
    @Column(precision = 10, scale = 2)
    private BigDecimal min_spend;
    @Column(precision = 10, scale = 2)
    private BigDecimal max_discount;

    private int usage_limit;
    private int usage_count;

    @Column(nullable = false)
    private LocalDateTime valid_from;
    @Column(nullable = false)
    private LocalDateTime valid_until;

    @OneToMany(mappedBy = "coupon")
    private Set<CouponRestriction> restrictions = new HashSet<>();
}
