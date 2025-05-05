package com.gotrid.trid.marshmello.repository;//package com.example.metamall.repository;
//
//import com.example.metamall.models.Shops.Shop;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface ShopRepository extends JpaRepository<Shop, Long> {
//
//    Optional<Shop> findByName(String name);
//    boolean existsByName(String name);
//
//    List<Shop> findByOwnerId(Long ownerId);
//    boolean existsByOwnerIdAndName(Long ownerId, String name);
//    Optional<Shop> findByIdAndOwnerId(Long shopId, Long ownerId);
//
//    @Query("SELECT s FROM Shop s JOIN s.authorizedClients u WHERE u.id = :userId")
//    List<Shop> findAccessibleByUserId(@Param("userId") Long userId);
//
//    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
//            "FROM Shop s WHERE s.id = :shopId AND (s.owner.id = :userId OR :userId IN " +
//            "(SELECT u.id FROM s.authorizedClients u))")
//    boolean hasUserAccess(@Param("userId") Long userId, @Param("shopId") Long shopId);
//}