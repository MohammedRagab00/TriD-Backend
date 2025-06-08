package com.gotrid.trid.core.cart.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CartItemId {
    private Integer cartId;
    private Integer productId;
}
