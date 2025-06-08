package com.gotrid.trid.core.product.repository;

import com.gotrid.trid.core.product.model.Product;
import com.gotrid.trid.core.product.model.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    Page<ProductVariant> findByProduct_Id(Integer productId, Pageable pageable);

    boolean existsByColorAndSizeAndProduct(String color, String size, Product product);
}
