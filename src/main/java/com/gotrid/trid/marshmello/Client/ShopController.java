package com.gotrid.trid.marshmello.Client;

import com.gotrid.trid.marshmello.dto.Shops.CoordinatesDTO;
import com.gotrid.trid.marshmello.dto.Shops.ShopRequest;
import com.gotrid.trid.marshmello.exception.ForbiddenAccessException;
import com.gotrid.trid.marshmello.models.Entities.User;
import com.gotrid.trid.marshmello.models.Shops.ClientsShops;
import com.gotrid.trid.marshmello.models.Shops.Coordinates;
import com.gotrid.trid.marshmello.models.Shops.DetailsShop;
import com.gotrid.trid.marshmello.models.Shops.Shop;
import com.gotrid.trid.marshmello.repository.ClientShopRepository;
import com.gotrid.trid.marshmello.repository.Shop.CoordinatesRepository;
import com.gotrid.trid.marshmello.repository.Shop.DetailsShopRepository;
import com.gotrid.trid.marshmello.repository.Shop.ShopRepository;
import com.gotrid.trid.marshmello.repository.UserRepository;
import com.gotrid.trid.marshmello.services.AzureFileService;
import com.gotrid.trid.marshmello.services.ShopServices;
import com.gotrid.trid.marshmello.services.shops.ClientServices;
import com.gotrid.trid.marshmello.utils.AuthorizationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/shops")
public class ShopController {

    @Autowired
    private ShopServices shopService;
    @Autowired
    ClientServices clientServices;
    @Autowired
    private AuthorizationUtil authorizationUtil;
    @Autowired
    private ClientShopRepository clientShopRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private DetailsShopRepository detailsShopRepository;
    @Autowired
    private CoordinatesRepository coordinatesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AzureFileService azureBlobService;
    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    public ShopController(ShopServices shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/createshop")
    public ResponseEntity<?> createShop(
            @RequestHeader("Authorization") String authHeader,
            @RequestPart(value = "gltfFile", required = true) MultipartFile gltfFile,
            @RequestPart(value = "binFile", required = true) MultipartFile binFile,
            @RequestPart(value = "iconFile", required = false) MultipartFile iconFile,
            @RequestPart(value = "textureFile", required = false) MultipartFile textureFile) {

        try {
            Long userId = authorizationUtil.validateUserRole(authHeader, "ROLE_CLIENT");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
            }

            if (gltfFile == null || binFile == null || iconFile == null || textureFile == null) {
                return ResponseEntity.badRequest().body("All files (gltf, bin, icon, texture) are required.");
            }

            Map<String, Object> response = clientServices.createShopWithFiles(
                    userId,
                    gltfFile,
                    binFile,
                    iconFile,
                    textureFile
            );

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating shop: " + e.getMessage());
        }
    }

    @PutMapping("/{shopId}/details")
    public ResponseEntity<?> addOrUpdateShopDetails(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long shopId,
            @RequestBody DetailsShop detailsDto) {
        try {
            Long userId = authorizationUtil.validateUserRole(authHeader, "ROLE_CLIENT");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            if (!shopService.verifyShopOwnership(shopId, userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            DetailsShop result = clientServices.addOrUpdateShopDetails(shopId, detailsDto);
            return ResponseEntity.ok(result);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }

    @PutMapping("/{shopId}/coordinates")
    public ResponseEntity<?> addOrUpdateShopCoordinates(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long shopId,
            @RequestBody CoordinatesDTO coordinatesDto) {

        try {
            Long userId = authorizationUtil.validateUserRole(authHeader, "ROLE_CLIENT");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Coordinates updatedCoordinates = clientServices.updateShopCoordinates(
                    shopId,
                    userId,
                    coordinatesDto
            );

            return ResponseEntity.ok(updatedCoordinates);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ForbiddenAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShopById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        System.out.println("hello");
        Optional<Shop> optionalShop = shopRepository.findById(id);
        if (optionalShop.isPresent()) {
            return ResponseEntity.ok(optionalShop.get());
        } else {
            return ResponseEntity.status(404).body("Shop not found with ID: " + id);
        }
    }

    @PostMapping(value = "/upload", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> createShop(
            @RequestHeader("Authorization") String authHeader,
            @RequestPart("shopRequest") String shopRequestJson, // ✅ expect nested JSON here
            @RequestPart(value = "gltfFile", required = false) MultipartFile gltfFile,
            @RequestPart(value = "binFile", required = false) MultipartFile binFile,
            @RequestPart(value = "iconFile", required = false) MultipartFile iconFile,
            @RequestPart(value = "textureFile", required = false) MultipartFile textureFile
    ) throws IOException {

        Long userId = authorizationUtil.validateUserRole(authHeader, "ROLE_CLIENT");
        System.out.println("userId is " + userId);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ShopRequest shopRequest = objectMapper.readValue(shopRequestJson, ShopRequest.class);        // Check if user has client role

        System.out.println(shopRequest.toString());
        Shop createdShop = shopService.createShopWithAssets(
                userId, shopRequest, gltfFile, binFile, iconFile, textureFile);
        System.out.println(createdShop.toString());


        ClientsShops clientShop = new ClientsShops();
        Optional<User> user = userRepository.findById(userId);
        clientShop.setClient(user.get());
        clientShop.setShop(createdShop);
        clientShopRepository.save(clientShop);
        return ResponseEntity.ok(createdShop);
    }

//    @GetMapping("/{shopId}/gltf")
//    public ResponseEntity<byte[]> getShopGltf(@PathVariable Long shopId) {
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(shopService.getShopGltfFile(shopId));
//    }
//
//    @DeleteMapping("/{shopId}")
//    public ResponseEntity<Void> deleteShop(@PathVariable Long shopId) {
//        shopService.deleteShopWithAssets(shopId);
//        return ResponseEntity.noContent().build();
//    }

    private void updateCoordinatesFromDto(Coordinates coordinates, CoordinatesDTO dto) {
        coordinates.setXPos(dto.getXpos());
        coordinates.setYPos(dto.getYpos());
        coordinates.setZPos(dto.getZpos());
        coordinates.setXScale(dto.getXscale());
        coordinates.setYScale(dto.getYscale());
        coordinates.setZScale(dto.getZscale());
        coordinates.setXRot(dto.getXrot());
        coordinates.setYRot(dto.getYrot());
        coordinates.setZRot(dto.getZrot());
    }

    private Coordinates mapDtoToCoordinates(CoordinatesDTO dto) {
        Coordinates coordinates = new Coordinates();
        coordinates.setXPos(dto.getXpos());
        coordinates.setYPos(dto.getYpos());
        coordinates.setZPos(dto.getZpos());
        coordinates.setXScale(dto.getXscale());
        coordinates.setYScale(dto.getYscale());
        coordinates.setZScale(dto.getZscale());
        coordinates.setXRot(dto.getXrot());
        coordinates.setYRot(dto.getYrot());
        coordinates.setZRot(dto.getZrot());
        return coordinates;
    }

    private void updateShopDetails(DetailsShop existing, DetailsShop incoming) {
        existing.setName(incoming.getName());
        existing.setType(incoming.getType());
        existing.setLocation(incoming.getLocation());
        existing.setDescription(incoming.getDescription());
        existing.setEmail(incoming.getEmail());
        existing.setPhone(incoming.getPhone());
        existing.setFacebook(incoming.getFacebook());
        // Add other fields as needed
    }
}