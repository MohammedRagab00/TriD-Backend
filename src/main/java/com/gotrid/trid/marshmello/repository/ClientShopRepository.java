package com.gotrid.trid.marshmello.repository;

import com.example.metamall.models.Shops.ClientsShops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientShopRepository extends JpaRepository<ClientsShops, Long> { // Custom query to find the relationship between client and shop
    Optional<ClientsShops> findByClientIdAndShopId(Long clientId, Long shopId);

    // Alternative: Direct exists check (more efficient for ownership verification)
    boolean existsByClientIdAndShopId(Long clientId, Long shopId);
}