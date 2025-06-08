package com.gotrid.trid.core.cart.repository;

import com.gotrid.trid.core.cart.model.Cart;
import com.gotrid.trid.core.product.model.ProductVariant;
import com.gotrid.trid.core.user.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser(Users user);

    @Query("SELECT ci.variant FROM Cart c JOIN c.cartItem ci WHERE c.user.id = :userId")
    Page<ProductVariant> findCartVariants(@Param("userId") Integer userId, Pageable pageable);
}
