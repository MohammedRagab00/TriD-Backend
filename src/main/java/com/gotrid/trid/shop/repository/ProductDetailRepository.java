package com.gotrid.trid.shop.repository;

import com.gotrid.trid.shop.domain.product.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {

    List<ProductDetail> findByName(String name);

    List<ProductDetail> findByBasePriceLessThan(Double maxPrice);

    List<ProductDetail> findByBasePriceGreaterThan(Double minPrice);

    List<ProductDetail> findByColorsContaining(String colors);
}
