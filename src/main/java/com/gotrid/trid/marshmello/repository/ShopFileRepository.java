package com.gotrid.trid.marshmello.repository;

import com.example.metamall.models.Entities.ShopFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface ShopFileRepository extends JpaRepository<ShopFile, Long> {

    // Existing methods
    List<ShopFile> findByUserId(Long userId);

    @Query("SELECT sf FROM ShopFile sf WHERE sf.id = :fileId AND sf.user.id = :userId")
    Optional<ShopFile> findByIdAndUserId(@Param("fileId") Long fileId,
                                         @Param("userId") Long userId);

    @Query("SELECT sf FROM ShopFile sf WHERE (sf.gltfPath = :filePath OR sf.binPath = :filePath) AND sf.user.id = :userId")
    Optional<ShopFile> findByUserIdAndPathContaining(@Param("userId") Long userId,
                                                     @Param("filePath") String filePath);

    // New methods you requested (updated for user instead of client)

    // Find by GLTF path only
    Optional<ShopFile> findByGltfPath(String gltfPath);

    // Find by BIN path only
    Optional<ShopFile> findByBinPath(String binPath);

    // Find all files created after a certain date
    List<ShopFile> findByCreatedAtAfter(LocalDateTime date);

    // Count files per user (renamed from client)
    @Query("SELECT COUNT(sf) FROM ShopFile sf WHERE sf.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);

    // Additional useful query
    @Query("SELECT sf FROM ShopFile sf WHERE sf.user.id = :userId AND sf.createdAt BETWEEN :startDate AND :endDate")
    List<ShopFile> findByUserAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
