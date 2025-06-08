package com.gotrid.trid.core.cart.model;

import com.gotrid.trid.core.product.model.ProductVariant;
import jakarta.persistence.*;
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
@EqualsAndHashCode
@Entity
public class CartItem {

    @EmbeddedId
    private CartItemId id;

    @ManyToOne
    @MapsId("cartId")
    @OnDelete(action = CASCADE)
    private Cart cart;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    @OnDelete(action = CASCADE)
    private ProductVariant productVariant;

    @Column(nullable = false)
    private Integer quantity;
}
