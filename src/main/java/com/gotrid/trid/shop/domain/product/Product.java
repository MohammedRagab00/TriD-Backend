package com.gotrid.trid.shop.domain.product;

import com.gotrid.trid.infrastructure.common.AuditableEntity;
import com.gotrid.trid.shop.domain.Shop;
import com.gotrid.trid.shop.model.AssetInfo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"details"})
@Entity
public class Product extends AuditableEntity {
    @Embedded
    private AssetInfo assetInfo;

    @ManyToOne
    private Shop shop;

    @OneToOne(cascade = CascadeType.ALL)
    private ProductDetail details;
}