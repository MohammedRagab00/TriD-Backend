package com.gotrid.trid.core.wishlist.repository;

import com.gotrid.trid.core.product.model.Product;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.core.wishlist.model.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    Optional<Wishlist> findByUser(Users user);

    @Query("SELECT p FROM Wishlist w JOIN w.products p WHERE w.user.id = :userId")
    Page<Product> findWishlistProducts(@Param("userId") Integer userId, Pageable pageable);
}
