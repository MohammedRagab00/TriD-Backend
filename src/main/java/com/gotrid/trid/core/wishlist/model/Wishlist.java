package com.gotrid.trid.core.wishlist.model;

import com.gotrid.trid.common.model.AuditableEntity;
import com.gotrid.trid.core.product.model.Product;
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
@EqualsAndHashCode(callSuper = true, exclude = {"products", "user"})
@Entity
public class Wishlist extends AuditableEntity {
    @OneToOne
    @OnDelete(action = CASCADE)
    private Users user;

    @ManyToMany
    @JoinTable(
            name = "wishlist_item"
            , joinColumns = @JoinColumn(name = "wishlist_id")
            , inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();
}
