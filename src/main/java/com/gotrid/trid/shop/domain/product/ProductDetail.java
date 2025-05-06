package com.gotrid.trid.shop.domain.product;

import com.gotrid.trid.infrastructure.common.AuditableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"variants"})
@Entity
public class ProductDetail extends AuditableEntity {

    private String name;
    private String sizes;
    private String colors;
    private String description;
    private Double basePrice;

    @OneToMany(mappedBy = "product")
    private List<ProductVariant> variants = new ArrayList<>();
}
