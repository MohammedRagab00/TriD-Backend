package com.gotrid.trid.core.product.model;

import com.gotrid.trid.common.model.AuditableEntity;
import com.gotrid.trid.core.threedModel.model.Model;
import com.gotrid.trid.core.shop.model.Shop;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"variants", "shop", "model"})
@Entity
public class Product extends AuditableEntity {

    @Column(length = 100)
    private String name;
    @Column(length = 1000)
    private String description;
    @Column(precision = 10, scale = 2)
    private BigDecimal basePrice;

    @OneToMany(mappedBy = "product")
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "model_asset_id")
    private Model model;

    @ManyToOne
    private Shop shop;
}