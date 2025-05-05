package com.gotrid.trid.marshmello.repository.Shop;


import com.example.metamall.models.Shops.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query methods can be added here
    // Example:
     List<Product> findByDetailsProductNameContaining(String name);
     List<Product> findByDetailsProductPriceBetween(Double minPrice, Double maxPrice);
}
