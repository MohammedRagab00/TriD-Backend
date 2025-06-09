package com.gotrid.trid.core.cart.repository;

import com.gotrid.trid.core.cart.model.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Page<CartItem> findByCart_User_Id(Integer cartUserId, Pageable pageable);
}
