package com.gotrid.trid.shop.controller;

import com.gotrid.trid.infrastructure.azure.ShopStorageService;
import com.gotrid.trid.security.userdetails.UserPrincipal;
import com.gotrid.trid.shop.domain.Shop;
import com.gotrid.trid.shop.dto.DetailsShopDTO;
import com.gotrid.trid.shop.model.Coordinates;
import com.gotrid.trid.shop.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/shops")
public class ShopController {
    private final ShopService shopService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> createShop(
            @RequestBody @Valid DetailsShopDTO detailsShopDTO,
            @AuthenticationPrincipal UserPrincipal principal) {

        Integer ownerId = principal.user().getId();
        shopService.createShop(ownerId, detailsShopDTO);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{shopId}/coordinates")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> updateShopCoordinates(
            @PathVariable Integer shopId,
            @RequestBody Coordinates coordinates,
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
            @RequestParam("icon") MultipartFile iconFile,
            @RequestParam("texture") MultipartFile textureFile,
            @AuthenticationPrincipal UserPrincipal principal) {

        Integer ownerId = principal.user().getId();
        shopService.uploadShopAssets(ownerId, shopId, gltfFile, binFile, iconFile, textureFile);
        return ResponseEntity.ok().build();
    }
}
