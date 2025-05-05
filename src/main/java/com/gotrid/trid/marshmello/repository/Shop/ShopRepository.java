package com.gotrid.trid.marshmello.repository.Shop;

import com.example.metamall.models.Shops.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    //Optional<Shop> findByShopName(String shopName);
}
