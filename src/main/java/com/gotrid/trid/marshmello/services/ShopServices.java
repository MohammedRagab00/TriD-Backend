package com.gotrid.trid.marshmello.services;

import com.example.metamall.dto.Shops.ShopRequest;
import com.example.metamall.mappers.ShopMapper;
import com.example.metamall.models.Shops.ClientsShops;
import com.example.metamall.models.Shops.Coordinates;
import com.example.metamall.models.Shops.DetailsShop;
import com.example.metamall.models.Shops.Shop;
import com.example.metamall.repository.ClientShopRepository;
import com.example.metamall.repository.Shop.CoordinatesRepository;
import com.example.metamall.repository.Shop.DetailsShopRepository;
import com.example.metamall.repository.Shop.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
public class ShopServices {

    private final ShopRepository shopRepository;
    private final AzureFileService azureBlobService;
    private final DetailsShopRepository detailsShopRepository;
    private final CoordinatesRepository coordinateRepository;


    @Autowired
    private ClientShopRepository clientShopRepository;


    public ShopServices(ShopRepository shopRepository,
                       AzureFileService azureBlobService,
                       DetailsShopRepository detailsShopRepository,
                       CoordinatesRepository coordinateRepository) {
        this.shopRepository = shopRepository;
        this.azureBlobService = azureBlobService;
        this.detailsShopRepository = detailsShopRepository;
        this.coordinateRepository = coordinateRepository;
    }
    @Transactional
    public Shop createShopWithAssets(Long userId,
                                     ShopRequest shopRequest,
                                     MultipartFile gltfFile,
                                     MultipartFile binFile,
                                     MultipartFile iconFile,
                                     MultipartFile textureFile) throws IOException {
        System.out.println(gltfFile);

        Coordinates coordinates = ShopMapper.toEntity(shopRequest.getCoordinates());
        DetailsShop detailsShop = ShopMapper.toEntity(shopRequest.getDetailsShop());
        DetailsShop savedDetailsShop = detailsShopRepository.save(detailsShop);
        Coordinates savedCoordinates = coordinateRepository.save(coordinates);



        // 3. Now create the Shop with the saved entities
        Shop shop = new Shop();
//        Long shopid =
        shop.setDetailsShop(savedDetailsShop);
        shop.setCoordinates(savedCoordinates);

        // 4. Handle file uploads and set paths
        String basePath = "shops/" + userId + "/" + shopRequest.getDetailsShop().getName() + "/shop/";

        if (gltfFile != null && !gltfFile.isEmpty()) {
            shop.setShopGltf(azureBlobService.uploadFile(basePath + "gltfFile", gltfFile));
        }

        if (binFile != null && !binFile.isEmpty()) {
            shop.setShopBin(azureBlobService.uploadFile(basePath + "binFile", binFile));
        }
        if (iconFile != null && !iconFile.isEmpty()) {
            shop.setShopIcon(azureBlobService.uploadFile(basePath + "iconFile", iconFile));
        }
        if (textureFile != null && !textureFile.isEmpty()) {
            shop.setTexture(azureBlobService.uploadFile(basePath + "textureFile", textureFile));
        }


        System.out.println(shop.toString());

        // handle other files similarly...

        // 5. Save and return the Shop (ID will be auto-generated)
        return shopRepository.save(shop);
    }

    public byte[] getShopGltfFile(Long shopId) throws IOException {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        return azureBlobService.getFile(shop.getShopGltf());
    }

    public byte[] getShopBinFile(Long shopId) throws IOException {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        return azureBlobService.getFile(shop.getShopBin());
    }

    public byte[] getShopIcon(Long shopId) throws IOException {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        return azureBlobService.getFile(shop.getShopIcon());
    }

    public byte[] getShopTexture(Long shopId) throws IOException {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        return azureBlobService.getFile(shop.getTexture());
    }

//    public void deleteShopWithAssets(Long shopId) {
//        Shop shop = shopRepository.findById(shopId)
//                .orElseThrow(() -> new RuntimeException("Shop not found"));
//
//        // Delete assets from Azure Blob Storage
//        if (shop.getShopGltf() != null) {
//            azureBlobService.deleteFile(shop.getShopGltf());
//        }
//        if (shop.getShopBin() != null) {
//            azureBlobService.deleteFile(shop.getShopBin());
//        }
//        if (shop.getShopIcon() != null) {
//            azureBlobService.deleteFile(shop.getShopIcon());
//        }
//        if (shop.getTexture() != null) {
//            azureBlobService.deleteFile(shop.getTexture());
//        }
//
//        // Delete from database
//        shopRepository.delete(shop);
//    }
    public boolean verifyShopOwnership(Long shopId, Long userId) {
        // First check if the shop exists
        if (!shopRepository.existsById(shopId)) {
            return false;
        }

        // Check the ownership through the ClientsShops relationship
        Optional<ClientsShops> relationship = clientShopRepository.findByClientIdAndShopId(userId, shopId);

        return relationship.isPresent();
    }

    // Alternative implementation using direct query
    public boolean verifyShopOwnershipAlternative(Long shopId, Long userId) {
        return clientShopRepository.existsByClientIdAndShopId(userId, shopId);
    }
}

