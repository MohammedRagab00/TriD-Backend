package com.gotrid.trid.marshmello.services;//package com.example.metamall.services;
//
//import com.example.metamall.dto.Shops.ShopRequest;
//import com.example.metamall.models.Entities.User;
//import com.example.metamall.models.Shops.ClientsShops;
//import com.example.metamall.models.Shops.Coordinates;
//import com.example.metamall.models.Shops.Shop;
//import com.example.metamall.repository.Shop.CoordinatesRepository;
//import com.example.metamall.repository.Shop.ShopRepository;
//import com.example.metamall.repository.UserRepository;
//import com.example.metamall.security.services.UserServices;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class ShopService {
//    private final ShopRepository shopRepository;
//    private final CoordinatesRepository coordinatesRepository;
//    private final AzureBlobStorageService azureBlobStorageService;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public Shop createShop(ShopRequest request, MultipartFile gltfFile, MultipartFile binFile, MultipartFile iconFile, String username) throws IOException {
//        // 1. Validate user is a client
//        User client = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        if (!client.isClient()) {
//            throw new RuntimeException("Only clients can create shops");
//        }
//
//        // 2. Upload files to Azure
//        String gltfUrl = azureBlobStorageService.uploadFile(gltfFile, UUID.randomUUID() + ".gltf");
//        String binUrl = azureBlobStorageService.uploadFile(binFile, UUID.randomUUID() + ".bin");
//        String iconUrl = azureBlobStorageService.uploadFile(iconFile, UUID.randomUUID() + ".png");
//
//        // 3. Save Coordinates
//        Coordinates coordinates = new Coordinates(client.getId(),
//                request.getXPos(), request.getYPos(), request.getZPos(),
//                request.getXScale(), request.getYScale(), request.getZScale(),
//                request.getXRot(), request.getYRot(), request.getZRot()
//        );
//        coordinatesRepository.save(coordinates);
//
//        // 4. Create Shop
//        Shop shop = new Shop();
//        shop.setShopName(request.getShopName());
//        shop.setGltfUrl(gltfUrl);
//        shop.setBinUrl(binUrl);
//        shop.setIconUrl(iconUrl);
//        shop.setCoordinates(coordinates);
//
//        // 5. Link ShopFile to User (optional tracking)
//        ClientsShops clientsShops = new ClientsShops();
//
//        clientsShops.setShop(shop);
//        clientsShops.setClient(client);
//        client.getShopFiles().add(clientsShops);
//
//
//        return shopRepository.save(shop);
//    }
//}
