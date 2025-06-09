package com.gotrid.trid.core.product.model;

import com.gotrid.trid.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"product"})
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        name = "uk_product_variant_color_size",
        columnNames = {"color", "size", "product"}
))
public class ProductVariant extends BaseEntity {
    @Column(length = 50)
    private String color;
    @Column(length = 20)
    private String size;
    private int stock;
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
}
