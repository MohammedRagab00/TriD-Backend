package com.gotrid.trid.shop.domain.product;

import com.gotrid.trid.infrastructure.common.AuditableEntity;
import com.gotrid.trid.shop.domain.Shop;
import com.gotrid.trid.shop.model.ModelAsset;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;

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

    private String name;
    private String sizes;
    private String colors;
    private String description;
    private Double basePrice;

    @OneToMany(mappedBy = "product")
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private ModelAsset modelAsset;

    @ManyToOne
    @OnDelete(action = CASCADE)
    private Shop shop;
}