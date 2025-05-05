package com.gotrid.trid.marshmello.repository.Shop;

import com.example.metamall.models.Shops.DetailsProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailsProductRepository extends JpaRepository<DetailsProduct, Long> {

    DetailsProduct findByName(String name);


    List<DetailsProduct> findByPriceLessThanEqual(Double maxPrice);
    List<DetailsProduct> findByPriceGreaterThanEqual(Double minPrice);
    List<DetailsProduct> findByCountGreaterThan(Integer minCount);
    List<DetailsProduct> findByColorsContaining(String color);
}
