package com.gotrid.trid.shop.controller;

import com.gotrid.trid.infrastructure.azure.ShopStorageService;
import com.gotrid.trid.security.userdetails.UserPrincipal;
import com.gotrid.trid.shop.dto.*;
import com.gotrid.trid.shop.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@Tag(name = "Shop", description = "Shop management APIs")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/shops")
public class ShopController {
    private final ShopService shopService;
    private final ShopStorageService shopStorageService;

    @Operation(summary = "Create a new shop", description = "Creates a new shop for a seller")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Shop created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Not a seller")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Integer> createShop(
            @RequestBody @Valid ShopDetailDTO dto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal) {
        Integer ownerId = principal.user().getId();
        Integer shopId = shopService.createShop(ownerId, dto);
        return ResponseEntity.created(URI.create("/api/v1/shops/" + shopId)).body(shopId);
    }

    @Operation(summary = "Get shop details", description = "Retrieves details of a specific shop")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shop details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    @GetMapping("/{shopId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShopDetailResponse> getShop(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId) {
        return ResponseEntity.ok(shopService.getShop(shopId));
    }

    @Operation(summary = "Update shop coordinates", description = "Updates the 3D coordinates of a shop")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Coordinates updated successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    @PutMapping("/{shopId}/coordinates")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> updateShopCoordinates(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId,
            @RequestBody CoordinateDTO coordinates,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal) {
        shopService.updateShopCoordinates(principal.user().getId(), shopId, coordinates);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Upload shop assets", description = "Uploads 3D model files and related assets for a shop")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assets uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid files"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PutMapping("/{shopId}/upload-assets")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> uploadShopFiles(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId,
            @Parameter(description = "GLTF model file") @RequestParam("gltf") MultipartFile gltfFile,
            @Parameter(description = "Binary data file") @RequestParam("bin") MultipartFile binFile,
            @Parameter(description = "Shop icon file") @RequestParam(value = "icon", required = false) MultipartFile iconFile,
            @Parameter(description = "Texture file") @RequestParam(value = "texture", required = false) MultipartFile textureFile,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal) {
        shopStorageService.uploadShopAssets(principal.user().getId(), shopId, gltfFile, binFile, iconFile, textureFile);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update shop details", description = "Updates the general information of a shop")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Shop updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PutMapping("/{shopId}/edit")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> editShop(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId,
            @RequestBody @Valid ShopDetailDTO dto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal) {
        shopService.updateShop(principal.user().getId(), shopId, dto);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Get shop assets", description = "Retrieves URLs and details of shop's 3D assets")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assets retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    @GetMapping("/{shopId}/assets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShopAssetsDTO> getShopAssets(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId) {
        return ResponseEntity.ok(shopService.getShopAssetDetails(shopId));
    }

    @Operation(summary = "Update shop social media", description = "Updates or adds social media links for a shop")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Social media updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PutMapping("/{shopId}/socials")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> updateShopSocial(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId,
            @RequestBody @Valid SocialDTO social,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal) {
        shopService.updateShopSocial(principal.user().getId(), shopId, social);
        return ResponseEntity.accepted().build();
    }
}