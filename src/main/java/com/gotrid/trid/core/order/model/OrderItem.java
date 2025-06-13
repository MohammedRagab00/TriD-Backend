package com.gotrid.trid.core.order.model;

import com.gotrid.trid.core.product.model.ProductVariant;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"order", "variant"})
@Entity
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;

    @ManyToOne
    @MapsId("orderId")
    private Order order;

    @ManyToOne
    @MapsId("variantId")
    private ProductVariant variant;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private int quantity;
}
