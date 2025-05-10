package com.gotrid.trid.shop.controller;

import com.gotrid.trid.infrastructure.common.PageResponse;
import com.gotrid.trid.security.userdetails.UserPrincipal;
import com.gotrid.trid.shop.dto.CoordinateDTO;
import com.gotrid.trid.shop.dto.ModelAssetsDTO;
import com.gotrid.trid.shop.dto.product.ProductRequest;
import com.gotrid.trid.shop.dto.product.ProductResponse;
import com.gotrid.trid.shop.dto.product.ProductVariantRequest;
import com.gotrid.trid.shop.dto.product.ProductVariantResponse;
import com.gotrid.trid.shop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/shop/{shopId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Integer> createProduct(
            @PathVariable Integer shopId,
            @RequestBody @Valid ProductRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        Integer ownerId = principal.user().getId();
        return ResponseEntity.ok(productService.createProduct(shopId, ownerId, request));
    }

    @PutMapping("/{productId}/coordinates")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> updateProductCoordinates(
            @PathVariable Integer productId,
            @RequestBody CoordinateDTO coordinates,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        Integer ownerId = principal.user().getId();
        productService.updateProductCoordinates(productId, ownerId, coordinates);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shop/{shopId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<ProductResponse>> getShopProducts(
            @PathVariable Integer shopId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getShopProducts(shopId, page, size));
    }

    @PostMapping("/{productId}/variants")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Integer> addProductVariant(
            @PathVariable Integer productId,
            @RequestBody @Valid ProductVariantRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        Integer ownerId = principal.user().getId();
        return ResponseEntity.ok(productService.addProductVariant(productId, ownerId, request));
    }

    @GetMapping("/{productId}/variants")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<ProductVariantResponse>> getProductVariants(
            @PathVariable Integer productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getProductVariants(productId, page, size));
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> updateProduct(
            @PathVariable Integer productId,
            @RequestBody @Valid ProductRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        Integer ownerId = principal.user().getId();
        productService.updateProduct(productId, ownerId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/variant/{variantId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> updateProductVariant(
            @PathVariable Integer variantId,
            @RequestBody @Valid ProductVariantRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        Integer ownerId = principal.user().getId();
        productService.updateProductVariant(variantId, ownerId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/variant/{variantId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> deleteProductVariant(
            @PathVariable Integer variantId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        Integer ownerId = principal.user().getId();
        productService.deleteProductVariant(variantId, ownerId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Integer productId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        Integer ownerId = principal.user().getId();
        productService.deleteProduct(productId, ownerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Upload product assets", description = "Uploads 3D model files and related assets for a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assets uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid files"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PutMapping("/{productId}/upload-assets")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> uploadProductFiles(
            @Parameter(description = "ID of the product") @PathVariable Integer productId,
            @Parameter(description = "GLTF model file") @RequestParam("gltf") MultipartFile gltfFile,
            @Parameter(description = "Binary data file") @RequestParam("bin") MultipartFile binFile,
            @Parameter(description = "Product icon file") @RequestParam(value = "icon", required = false) MultipartFile iconFile,
            @Parameter(description = "Texture file") @RequestParam(value = "texture", required = false) MultipartFile textureFile,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        Integer ownerId = principal.user().getId();
        productService.uploadProductAssets(productId, ownerId, gltfFile, binFile, iconFile, textureFile);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get product assets", description = "Retrieves URLs and details of product's 3D assets")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assets retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}/assets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ModelAssetsDTO> getProductAssets(
            @Parameter(description = "ID of the product") @PathVariable Integer productId
    ) {
        return ResponseEntity.ok(productService.getProductAssetDetails(productId));
    }
}