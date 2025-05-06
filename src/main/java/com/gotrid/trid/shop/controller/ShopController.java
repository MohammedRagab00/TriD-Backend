package com.gotrid.trid.shop.controller;

import com.gotrid.trid.infrastructure.azure.ShopStorageService;
import com.gotrid.trid.security.userdetails.UserPrincipal;
import com.gotrid.trid.shop.dto.*;
import com.gotrid.trid.shop.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/shops")
public class ShopController {
    private final ShopService shopService;
    private final ShopStorageService shopStorageService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Integer> createShop(
            @RequestBody @Valid ShopDetailDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {

        Integer ownerId = principal.user().getId();
        Integer shopId = shopService.createShop(ownerId, dto);

        return ResponseEntity
                .created(URI.create("/api/v1/shops/" + shopId))
                .body(shopId);
    }

    @GetMapping("/{shopId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShopDetailResponse> getShop(@PathVariable Integer shopId) {
        ShopDetailResponse shop = shopService.getShop(shopId);
        return ResponseEntity.ok(shop);
    }

    @PutMapping("/{shopId}/coordinates")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> updateShopCoordinates(
            @PathVariable Integer shopId,
            @RequestBody CoordinateDTO coordinates,
            @AuthenticationPrincipal UserPrincipal principal) {

        Integer ownerId = principal.user().getId();
        shopService.updateShopCoordinates(ownerId, shopId, coordinates);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{shopId}/upload-assets")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> uploadShopFiles(
            @PathVariable Integer shopId,
            @RequestParam("gltf") MultipartFile gltfFile,
            @RequestParam("bin") MultipartFile binFile,
            @RequestParam(value = "icon", required = false) MultipartFile iconFile,
            @RequestParam(value = "texture", required = false) MultipartFile textureFile,
            @AuthenticationPrincipal UserPrincipal principal) {

        Integer ownerId = principal.user().getId();
        shopStorageService.uploadShopAssets(ownerId, shopId, gltfFile, binFile, iconFile, textureFile);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{shopId}/edit")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> editShop(
            @PathVariable Integer shopId,
            @RequestBody @Valid ShopDetailDTO dto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Integer ownerId = principal.user().getId();
        shopService.updateShop(ownerId, shopId, dto);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{shopId}/assets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShopAssetsDTO> getShopAssets(@PathVariable Integer shopId) {
        ShopAssetsDTO assets = shopService.getShopAssetDetails(shopId);
        return ResponseEntity.ok(assets);
    }

    @PutMapping("/{shopId}/socials")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> updateShopSocial(
            @PathVariable Integer shopId,
            @RequestBody @Valid SocialDTO social,
            @AuthenticationPrincipal UserPrincipal principal) {

        Integer ownerId = principal.user().getId();
        shopService.updateShopSocial(ownerId, shopId, social);
        return ResponseEntity.accepted().build();
    }
}
