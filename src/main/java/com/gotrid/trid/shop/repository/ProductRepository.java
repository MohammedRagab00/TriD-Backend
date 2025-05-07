package com.gotrid.trid.shop.repository;


import com.gotrid.trid.shop.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContaining(String name);

    List<Product> findByBasePriceBetween(Double minPrice, Double maxPrice);
}
