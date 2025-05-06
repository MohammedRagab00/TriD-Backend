package com.gotrid.trid.shop.model;

import com.gotrid.trid.infrastructure.common.BaseEntity;
import com.gotrid.trid.shop.domain.ShopDetail;
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
@EqualsAndHashCode(callSuper = true, exclude = {"shop"})
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"platform", "shop_id"})
)
public class Social extends BaseEntity {
    @Column(nullable = false)
    String platform;
    @Column(nullable = false)
    String link;

    @ManyToOne
    private ShopDetail shop;
}
