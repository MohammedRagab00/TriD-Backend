package com.gotrid.trid.core.cart.model;

import com.gotrid.trid.common.model.AuditableEntity;
import com.gotrid.trid.core.user.model.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;

import java.util.HashSet;
import java.util.Set;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"cartItem"})
@Entity
public class Cart extends AuditableEntity {

    @OneToOne
    @OnDelete(action = CASCADE)
    private Users user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItem = new HashSet<>();

}
