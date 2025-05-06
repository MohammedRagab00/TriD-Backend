package com.gotrid.trid.shop.repository;


import com.gotrid.trid.shop.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByDetails_NameContaining(String name);

    List<Product> findByDetails_BasePriceBetween(Double minPrice, Double maxPrice);
}
