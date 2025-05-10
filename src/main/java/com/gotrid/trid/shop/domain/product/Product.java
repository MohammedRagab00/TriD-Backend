package com.gotrid.trid.shop.domain.product;

import com.gotrid.trid.infrastructure.common.AuditableEntity;
import com.gotrid.trid.shop.domain.Shop;
import com.gotrid.trid.shop.model.ModelAsset;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"variants", "shop", "modelAsset"})
@Entity
public class Product extends AuditableEntity {

    @Column(length = 100)
    private String name;
    @Column(length = 50)
    private String sizes;
    @Column(length = 50)
    private String colors;
    @Column(length = 1000)
    private String description;
    @Column(precision = 10, scale = 2)
    private BigDecimal basePrice;

    @OneToMany(mappedBy = "product")
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private ModelAsset modelAsset;

    @ManyToOne
    private Shop shop;
}