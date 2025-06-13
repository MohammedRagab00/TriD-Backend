package com.gotrid.trid.core.product.repository;


import com.gotrid.trid.core.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

//    Page<Product> findByBasePriceBetween(BigDecimal basePrice, BigDecimal basePrice2, Pageable pageable);

    Page<Product> findByShopId(Integer shopId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
