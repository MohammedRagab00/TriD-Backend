package com.gotrid.trid.shop.domain.product;

import com.gotrid.trid.infrastructure.common.BaseEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"product"})
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"color", "size"})
)
public class ProductVariant extends BaseEntity {
    private String color;
    @Column(length = 20)
    private String size;
    private int stock;
    private double price;

    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;
}
