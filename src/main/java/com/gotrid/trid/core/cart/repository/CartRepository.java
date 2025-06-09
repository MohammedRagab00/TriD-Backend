package com.gotrid.trid.core.cart.repository;

import com.gotrid.trid.core.cart.model.Cart;
import com.gotrid.trid.core.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser(Users user);
}
