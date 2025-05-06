package com.gotrid.trid.shop.repository;

import com.gotrid.trid.shop.domain.ShopDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopDetailRepository extends JpaRepository<ShopDetail, Integer> {

    ShopDetail findByName(String name);

    List<ShopDetail> findByLocationContaining(String location);

    List<ShopDetail> findByCategory(String category);
}
