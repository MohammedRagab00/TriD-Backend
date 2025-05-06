package com.gotrid.trid.shop.repository;

import com.gotrid.trid.shop.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
}
