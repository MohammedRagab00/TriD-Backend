package com.gotrid.trid.shop.domain;

import com.gotrid.trid.infrastructure.common.AuditableEntity;
import com.gotrid.trid.shop.domain.product.Product;
import com.gotrid.trid.shop.model.ModelAsset;
import com.gotrid.trid.shop.model.Social;
import com.gotrid.trid.user.domain.Users;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"socials", "products", "modelAsset", "owner"})
@Entity
public class Shop extends AuditableEntity {

    @Column(unique = true, nullable = false, length = 40)
    private String name;
    @Column(length = 40)
    private String category;
    private String location;

    @Column(length = 1000, nullable = false)
    private String description;
    @Column(length = 60)
    private String email;
    @Column(length = 20)
    private String phone;

    @OneToMany(
            mappedBy = "shop",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Social> socials = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private ModelAsset modelAsset;

    @OneToMany(mappedBy = "shop")
    private List<Product> products = new ArrayList<>();

    @ManyToOne
    @OnDelete(action = CASCADE)
    private Users owner;
}