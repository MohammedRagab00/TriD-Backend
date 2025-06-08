package com.gotrid.trid.core.wishlist.repository;

import com.gotrid.trid.core.wishlist.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
}
