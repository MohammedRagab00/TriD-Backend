package com.gotrid.trid.api.shop.controller;

import com.gotrid.trid.api.shop.dto.CoordinateDTO;
import com.gotrid.trid.api.shop.dto.ModelAssetsDTO;
import com.gotrid.trid.api.shop.dto.product.ProductRequest;
import com.gotrid.trid.api.shop.dto.product.ProductResponse;
import com.gotrid.trid.api.shop.dto.product.ProductVariantRequest;
import com.gotrid.trid.api.shop.dto.product.ProductVariantResponse;
import com.gotrid.trid.api.shop.service.ProductService;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.config.security.userdetails.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "Product management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Create product", description = "Creates a new product for a shop")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    @PostMapping("/shop/{shopId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Integer> createProduct(
            @PathVariable Integer shopId,
            @RequestBody @Valid ProductRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal
    ) {
        Integer ownerId = principal.user().getId();
        Integer productId = productService.createProduct(shopId, ownerId, request);
        return ResponseEntity.created(URI.create("/api/v1/products/" + productId)).body(productId);
    }

    @Operation(summary = "Update product coordinates", description = "Updates 3D coordinates")
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

    @Operation(summary = "Get shop products", description = "Retrieves all products of a shop")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    @GetMapping("/shop/{shopId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<ProductResponse>> getShopProducts(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId,

            @Parameter(description = "Page number", schema = @Schema(defaultValue = "0"))
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Items per page", schema = @Schema(defaultValue = "10"))
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getShopProducts(shopId, page, size));
    }

    @Operation(summary = "Add product variant", description = "Adds a new variant to product")
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

    @Operation(summary = "Get product variants", description = "Lists all variants of a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}/variants")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<ProductVariantResponse>> getProductVariants(
            @PathVariable Integer productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.getProductVariants(productId, page, size));
    }

    @Operation(summary = "Update product", description = "Updates product details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
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

    @Operation(summary = "Update product variant", description = "Updates variant details")
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

    @Operation(summary = "Delete product variant", description = "Removes a variant")
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

    @Operation(summary = "Delete product", description = "Deletes a product and its variants")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
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

    @Operation(summary = "Get product", description = "Retrieves details of a specific product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "ID of the product") @PathVariable Integer productId
    ) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }
}