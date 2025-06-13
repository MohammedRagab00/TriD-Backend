package com.gotrid.trid.core.order.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class OrderItemId {
    private Integer orderId;
    private Integer variantId;
}
