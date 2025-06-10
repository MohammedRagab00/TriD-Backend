package com.gotrid.trid.core.coupon.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "coupon")
@Entity
@Table(name = "coupon_restrictions")
public class CouponRestriction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @OnDelete(action = CASCADE)
    private Coupon coupon;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Restriction restrictionType;

    @Column(nullable = false)
    private Integer restrictionValue;
}
