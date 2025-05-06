package com.gotrid.trid.shop.domain;

import com.gotrid.trid.infrastructure.common.AuditableEntity;
import com.gotrid.trid.shop.domain.product.Product;
import com.gotrid.trid.shop.model.AssetInfo;
import com.gotrid.trid.user.domain.Users;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"assetInfo", "products", "shopDetail"})
@Entity
public class Shop extends AuditableEntity {
    @Embedded
    private AssetInfo assetInfo;

    @OneToOne(cascade = CascadeType.ALL)
    private ShopDetail shopDetail;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users owner;
}