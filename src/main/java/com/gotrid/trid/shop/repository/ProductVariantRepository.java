package com.gotrid.trid.shop.repository;

import com.gotrid.trid.shop.domain.product.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    boolean existsByColorAndSize(String color, String size);

    Page<ProductVariant> findByProduct_Id(Integer productId, Pageable pageable);

    Optional<ProductVariant> findByIdAndProductId(Integer id, Integer productId);
}
