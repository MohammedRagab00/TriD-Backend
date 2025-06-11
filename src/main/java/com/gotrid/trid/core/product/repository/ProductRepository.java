package com.gotrid.trid.core.product.repository;


import com.gotrid.trid.core.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByBasePriceBetween(BigDecimal basePrice, BigDecimal basePrice2);

    Page<Product> findByShopId(Integer shopId, Pageable pageable);

    Page<Product> findByNameContaining(String name, Pageable pageable);
}
