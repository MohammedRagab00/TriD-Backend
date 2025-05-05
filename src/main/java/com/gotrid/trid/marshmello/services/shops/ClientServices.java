package com.gotrid.trid.marshmello.services.shops;

import com.example.metamall.dto.Shops.CoordinatesDTO;
import com.example.metamall.exception.ForbiddenAccessException;
import com.example.metamall.models.Shops.*;
import com.example.metamall.repository.ClientShopRepository;
import com.example.metamall.repository.Shop.CoordinatesRepository;
import com.example.metamall.repository.Shop.DetailsShopRepository;
import com.example.metamall.repository.Shop.ProductRepository;
import com.example.metamall.repository.Shop.ShopRepository;
import com.example.metamall.repository.UserRepository;
import com.example.metamall.services.AzureFileService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class ClientServices {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CoordinatesRepository coordinatesRepository;
    @Autowired
    private DetailsShopRepository detailsShopRepository;
    @Autowired
    private AzureFileService azureFileService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientShopRepository clientShopRepository;
    @Autowired
    private  ProductRepository productRepository;

    public Map<String, Object> createShopWithFiles(
            Long userId,
            MultipartFile gltfFile,
            MultipartFile binFile,
            MultipartFile iconFile,
            MultipartFile textureFile) throws IOException {

        // Create and save shop entity first to get the ID
        Shop shop = new Shop();
        shop = shopRepository.save(shop);

        // Create client-shop relationship
        ClientsShops clientsShops = new ClientsShops();
        clientsShops.setClient(userRepository.getReferenceById(userId));
        clientsShops.setShop(shopRepository.getReferenceById(shop.getId()));
        clientShopRepository.save(clientsShops);

        // Build base path with generated shop ID
        String basePath = String.format("users/%d/shops/%d", userId, shop.getId());

        // Upload and set files
        if (!gltfFile.isEmpty()) {
            String url = azureFileService.uploadFile(basePath + "/models/" + gltfFile.getOriginalFilename(), gltfFile);
            shop.setShopGltf(url);
        }

        if (!binFile.isEmpty()) {
            String url = azureFileService.uploadFile(basePath + "/binaries/" + binFile.getOriginalFilename(), binFile);
            shop.setShopBin(url);
        }

        if (!iconFile.isEmpty()) {
            String url = azureFileService.uploadFile(basePath + "/icons/" + iconFile.getOriginalFilename(), iconFile);
            shop.setShopIcon(url);
        }

        if (!textureFile.isEmpty()) {
            String url = azureFileService.uploadFile(basePath + "/textures/" + textureFile.getOriginalFilename(), textureFile);
            shop.setTexture(url);
        }

        // Save updated shop with file URLs
        shopRepository.save(shop);

        return Map.of(
                "shopId", shop.getId(),
                "message", "Shop created successfully with files"
        );
    }

    public Map<String, Object> createProductWithFiles(
            Long userId,
            Long shopId,
            MultipartFile gltfFile,
            MultipartFile binFile,
            MultipartFile iconFile,
            MultipartFile textureFile) throws IOException {

        // Create and save shop entity first to get the ID
        // Verify shop ownership
        if (!verifyShopOwnership(shopId, userId)) {
            throw new ForbiddenAccessException("User doesn't own this shop");
        }

        // Fetch shop
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found"));


        Product product=new Product();
        product.setShop(shop);

        // Build base path with generated shop ID
        String basePath = String.format("users/%d/shops/%d", userId, product.getId());

        // Upload and set files
        if (!gltfFile.isEmpty()) {
            String url = azureFileService.uploadFile(basePath + "/models/" + gltfFile.getOriginalFilename(), gltfFile);
            product.setProductGltf(url);
        }

        if (!binFile.isEmpty()) {
            String url = azureFileService.uploadFile(basePath + "/binaries/" + binFile.getOriginalFilename(), binFile);
            product.setProductBin(url);
        }

        if (!iconFile.isEmpty()) {
            String url = azureFileService.uploadFile(basePath + "/icons/" + iconFile.getOriginalFilename(), iconFile);
            product.setProductIcon(url);
        }

        if (!textureFile.isEmpty()) {
            String url = azureFileService.uploadFile(basePath + "/textures/" + textureFile.getOriginalFilename(), textureFile);
            product.setTexture(url);
        }

        // Save updated shop with file URLs
        productRepository.save(product);

        return Map.of(
                "productId", product.getId(),
                "message", "Product created successfully with files"
        );
    }

    public Coordinates updateShopCoordinates(
            Long shopId,
            Long userId,
            CoordinatesDTO coordinatesDto) {

        // Verify shop ownership
        if (!verifyShopOwnership(shopId, userId)) {
            throw new ForbiddenAccessException("User doesn't own this shop");
        }

        // Fetch shop
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found"));

        // Update or create coordinates
        Coordinates coordinates = shop.getCoordinates();

        if (coordinates != null) {
            // Existing: update fields
            updateCoordinatesFromDto(coordinates, coordinatesDto);
        } else {
            // New: create and assign to shop
            coordinates = createNewCoordinates(coordinatesDto);
            shop.setCoordinates(coordinates);
            shopRepository.save(shop);
        }

        return coordinatesRepository.save(coordinates);
    }

    private Coordinates createNewCoordinates(CoordinatesDTO dto) {
        Coordinates coordinates = new Coordinates();
        updateCoordinatesFromDto(coordinates, dto);
        return coordinates;
    }

    public DetailsShop addOrUpdateShopDetails(Long shopId, DetailsShop detailsDto) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found"));

        DetailsShop details = shop.getDetailsShop();

        if (details != null) {
            updateShopDetails(details, detailsDto);
        } else {
            details = new DetailsShop();
            updateShopDetails(details, detailsDto);
            details = detailsShopRepository.save(details);
            shop.setDetailsShop(details);
            shopRepository.save(shop);
        }

        return detailsShopRepository.save(details);
    }

    public Coordinates updateOrCreateCoordinates(Shop shop, CoordinatesDTO dto) {
        Coordinates coordinates = shop.getCoordinates();
        if (coordinates == null) {
            coordinates = new Coordinates();
        }
        updateCoordinatesFromDto(coordinates, dto);
        coordinates = coordinatesRepository.save(coordinates);
        shop.setCoordinates(coordinates);
        shopRepository.save(shop);
        return coordinates;
    }

    private void updateCoordinatesFromDto(Coordinates c, CoordinatesDTO dto) {
        c.setXPos(dto.getXpos());
        c.setYPos(dto.getYpos());
        c.setZPos(dto.getZpos());
        c.setXScale(dto.getXscale());
        c.setYScale(dto.getYscale());
        c.setZScale(dto.getZscale());
        c.setXRot(dto.getXrot());
        c.setYRot(dto.getYrot());
        c.setZRot(dto.getZrot());
    }

    public DetailsShop updateOrCreateDetails(Shop shop, DetailsShop newDetails) {
        DetailsShop existing = shop.getDetailsShop();
        if (existing == null) {
            existing = detailsShopRepository.save(newDetails);
        } else {
            existing.setName(newDetails.getName());
            existing.setDescription(newDetails.getDescription());
            existing.setEmail(newDetails.getEmail());
            existing.setPhone(newDetails.getPhone());
            // update more fields...
            detailsShopRepository.save(existing);
        }
        shop.setDetailsShop(existing);
        shopRepository.save(shop);
        return existing;
    }

    public boolean verifyShopOwnership(Long shopId, Long userId) {
        // First check if the shop exists
        if (!shopRepository.existsById(shopId)) {
            return false;
        }

        // Check the ownership through the ClientsShops relationship
        Optional<ClientsShops> relationship = clientShopRepository.findByClientIdAndShopId(userId, shopId);

        return relationship.isPresent();
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
