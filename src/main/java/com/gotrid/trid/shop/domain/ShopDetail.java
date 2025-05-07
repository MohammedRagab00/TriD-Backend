package com.gotrid.trid.shop.domain;

import com.gotrid.trid.infrastructure.common.AuditableEntity;
import com.gotrid.trid.shop.model.Social;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;

import java.util.HashSet;
import java.util.Set;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"socials"})
@Entity
public class ShopDetail extends AuditableEntity {
    @OneToOne
    @MapsId
    @OnDelete(action = CASCADE)
    private Shop shop;

    @Column(unique = true, nullable = false, length = 40)
    private String name;
    @Column(length = 40)
    private String category;
    private String location;

    @Column(length = 1000, nullable = false)
    private String description;
    private String email;
    private String phone;

    @OneToMany(
            mappedBy = "shopDetail",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Social> socials = new HashSet<>();

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
