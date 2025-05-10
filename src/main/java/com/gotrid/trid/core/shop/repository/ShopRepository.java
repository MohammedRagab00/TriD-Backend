package com.gotrid.trid.core.shop.repository;

import com.gotrid.trid.core.shop.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
    boolean existsByNameIgnoreCase(String name);
}
