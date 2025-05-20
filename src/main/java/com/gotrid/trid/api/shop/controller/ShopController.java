package com.gotrid.trid.api.shop.controller;

import com.gotrid.trid.core.threedModel.dto.CoordinateDTO;
import com.gotrid.trid.api.shop.dto.SocialDTO;
import com.gotrid.trid.api.shop.dto.ShopModelResponse;
import com.gotrid.trid.api.shop.dto.ShopRequest;
import com.gotrid.trid.api.shop.dto.ShopResponse;
import com.gotrid.trid.api.shop.dto.ShopUpdateRequest;
import com.gotrid.trid.api.shop.service.ShopService;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.config.security.userdetails.UserPrincipal;
import com.gotrid.trid.infrastructure.azure.ShopStorageService;
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
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Shop", description = "Shop management APIs")
@SecurityRequirement(name = "Bearer Authentication")
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
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Integer> createShop(
            @RequestBody @Valid ShopRequest dto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal) {
        Integer ownerId = principal.user().getId();
        Integer shopId = shopService.createShop(ownerId, dto);
        return ResponseEntity.created(URI.create("/api/v1/shops/" + shopId)).body(shopId);
    }

    @Operation(summary = "Upload/update shop logo", description = "Uploads or updates the shop logo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logo updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PostMapping(value = "/{shopId}/logo", consumes = MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> uploadShopLogo(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId,
            @Parameter(description = "Shop logo file") @RequestParam("logo") MultipartFile logoFile,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal) {
        shopStorageService.uploadShopLogo(principal.user().getId(), shopId, logoFile);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Upload shop model", description = "Uploads 3D GLB model file for a shop")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Model uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PostMapping(value = "/{shopId}/model", consumes = MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> uploadShopModel(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId,
            @Parameter(description = "GLB model file") @RequestParam("glb") MultipartFile glbFile,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal) {
        shopStorageService.uploadShopAssets(principal.user().getId(), shopId, glbFile);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Set shop coordinates", description = "Sets the 3D coordinates of a shop")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coordinates set successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    @PutMapping("/{shopId}/coordinates")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> setShopCoordinates(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId,
            @RequestBody CoordinateDTO coordinates,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal) {
        shopService.updateShopCoordinates(principal.user().getId(), shopId, coordinates);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Add shop photos", description = "Uploads one or multiple shop photos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Photos added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid files"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PostMapping(value = "/{shopId}/photos", consumes = MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> addShopPhotos(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId,
            @Parameter(description = "Shop photo files") @RequestParam("photos") List<MultipartFile> photoFiles,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal) {
        shopStorageService.uploadShopPhotos(principal.user().getId(), shopId, photoFiles);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Partially update shop details", description = "Accepts partial updates including logo, photos, and model asset")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Shop updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PatchMapping(value = "/{shopId}", consumes = MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> patchShop(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId,
            @ModelAttribute ShopUpdateRequest shopUpdateRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal) {

        shopService.patchShop(principal.user().getId(), shopId, shopUpdateRequest);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Get shop details", description = "Retrieves details of a specific shop")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shop details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    @GetMapping("/{shopId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShopResponse> getShop(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId) {
        return ResponseEntity.ok(shopService.getShop(shopId));
    }

    @Operation(summary = "Get all shops", description = "Retrieves all shops with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shops retrieved successfully")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<ShopResponse>> getAllShops(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return ResponseEntity.ok(shopService.getAllShops(page, size));
    }

    @Operation(summary = "Get shop assets", description = "Retrieves URLs and details of shop's 3D assets")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assets retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    @GetMapping("/{shopId}/assets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShopModelResponse> getShopAssets(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId) {
        return ResponseEntity.ok(shopService.getShopModelDetails(shopId));
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


    @Operation(summary = "Delete shop", description = "Deletes a shop and all its associated products and assets")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Shop deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    @DeleteMapping("/{shopId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> deleteShop(
            @Parameter(description = "ID of the shop") @PathVariable Integer shopId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal principal) {
        shopService.deleteShop(principal.user().getId(), shopId);
        return ResponseEntity.noContent().build();
    }
}