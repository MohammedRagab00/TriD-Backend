package com.gotrid.trid.core.coupon.model;

import com.gotrid.trid.common.model.BaseEntity;
import com.gotrid.trid.core.order.model.Order;
import com.gotrid.trid.core.user.model.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"coupon", "user", "order"})
@Entity
@Table(name = "coupon_usages")
public class CouponUsage extends BaseEntity {
    @ManyToOne(optional = false)
    @OnDelete(action = CASCADE)
    private Coupon coupon;

    @ManyToOne(optional = false)
    @OnDelete(action = CASCADE)
    private Users user;

    @OneToOne
    @OnDelete(action = CASCADE)
    private Order order;
}
