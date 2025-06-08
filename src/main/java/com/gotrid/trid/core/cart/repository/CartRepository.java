package com.gotrid.trid.core.cart.repository;

import com.gotrid.trid.core.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
