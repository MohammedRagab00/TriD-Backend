package com.gotrid.trid.core.shop.repository;


import com.gotrid.trid.core.shop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContaining(String name);

    List<Product> findByBasePriceBetween(BigDecimal basePrice, BigDecimal basePrice2);

    Page<Product> findByShopId(Integer shopId, Pageable pageable);

    Optional<Product> findByIdAndShopId(Integer id, Integer shopId);
}
