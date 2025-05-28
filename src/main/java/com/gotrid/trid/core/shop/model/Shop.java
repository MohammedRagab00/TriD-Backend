package com.gotrid.trid.core.shop.model;

import com.gotrid.trid.common.model.AuditableEntity;
import com.gotrid.trid.core.product.model.Product;
import com.gotrid.trid.core.threedModel.model.Model;
import com.gotrid.trid.core.user.model.Users;
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
@EqualsAndHashCode(callSuper = true, exclude = {"socials", "products", "model", "owner"})
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
    @JoinColumn(name = "model_asset_id")
    private Model model;

    @Column(length = 100)
    private String logo;

    @OneToMany(mappedBy = "shop")
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @OnDelete(action = CASCADE)
    private Users owner;
}