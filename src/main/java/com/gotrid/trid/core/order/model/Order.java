package com.gotrid.trid.core.order.model;

import com.gotrid.trid.common.model.AuditableEntity;
import com.gotrid.trid.core.user.model.Users;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"customer"})
@Entity
@Table(name = "orders")
public class Order extends AuditableEntity {

    @ManyToOne(optional = false)
    @OnDelete(action = CASCADE)
    private Users customer;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total_amount;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private Set<OrderItem> orderItems = new HashSet<>();

    @Column(length = 200)
    private String address;
    @Column(precision = 9, scale = 6, nullable = false)
    BigDecimal latitude;
    @Column(precision = 9, scale = 6, nullable = false)
    BigDecimal longitude;
    @Column(length = 10)
    private String phone_number;
    @Column(length = 100)
    private String landmark;
}
