package com.gotrid.trid.marshmello.repository.Shop;

import com.example.metamall.models.Shops.DetailsShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailsShopRepository extends JpaRepository<DetailsShop, Long> {
    // Find by unique name
    DetailsShop findByName(String name);


     List<DetailsShop> findByLocationContaining(String location);
     List<DetailsShop> findByType(String type);
}
