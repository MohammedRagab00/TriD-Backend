package com.gotrid.trid.shop.repository;


import com.gotrid.trid.shop.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContaining(String name);

    List<Product> findByBasePriceBetween(Double minPrice, Double maxPrice);

    Page<Product> findByShopId(Integer shopId, Pageable pageable);

    Optional<Product> findByIdAndShopId(Integer id, Integer shopId);
}
