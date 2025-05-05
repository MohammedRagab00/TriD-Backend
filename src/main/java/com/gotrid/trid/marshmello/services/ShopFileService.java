package com.gotrid.trid.marshmello.services;//package com.example.metamall.services;
//
//import com.example.metamall.exception.ResourceNotFoundException;
//import com.example.metamall.models.Entities.User;
//import com.example.metamall.models.Shops.Product;
//import com.example.metamall.dto.Users.ProductDto;
//import com.example.metamall.models.Shops.Coordinate;
//import com.example.metamall.models.Shops.Shops;
//import com.example.metamall.repository.CoordinateRepository;
//import com.example.metamall.repository.ProductRepository;
//import com.example.metamall.repository.ShopRepository;
//import com.example.metamall.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Service
//@RequiredArgsConstructor
//public class ShopFileService {
//
//    private final ShopRepository shopRepository;
//    private final AzureBlobService azureStorage;
//    private final UserRepository userRepository;
//    private final ProductRepository productRepository;
//    private final CoordinateRepository coordinateRepository;
//
//    @Transactional
//    public Shops createShopWithFiles(Long ownerId, String shopName,
//                                     MultipartFile gltfFile,
//                                     MultipartFile binFile,
//                                     MultipartFile iconFile) throws IOException {
//
//        // Validate unique shop name for user
//        if (shopRepository.existsByOwnerIdAndName(ownerId, shopName)) {
//            throw new IllegalArgumentException("Shop name already exists for this user");
//        }
//
//        // Upload files to Azure
//        String container = "shop-files";
//        String gltfPath = azureStorage.uploadFile(gltfFile, container, shopName + "/models/" + gltfFile.getOriginalFilename());
//        String binPath = azureStorage.uploadFile(binFile, container, shopName + "/models/" + binFile.getOriginalFilename());
//        String iconPath = azureStorage.uploadFile(iconFile, container, shopName + "/icons/" + iconFile.getOriginalFilename());
//
//        // Create shop entity
//        User owner = userRepository.findById(ownerId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        Shops shop = new Shops();
//        shop.setName(shopName);
//        shop.setGltfFileUrl(gltfPath);
//        shop.setBinFileUrl(binPath);
//        shop.setIconUrl(iconPath);
//
//        return shopRepository.save(shop);
//    }
//
//    @Transactional
//    public void addProductWithCoordinates(Long shopId, ProductDto productDto,
//                                          MultipartFile gltfFile, MultipartFile binFile) throws IOException {
//
//        Shops shop = shopRepository.findById(shopId)
//                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));
//
//        String container = "shop-files";
//        String gltfPath = azureStorage.uploadFile(gltfFile, container, shop.getName() + "/products/gltf/" + gltfFile.getOriginalFilename());
//        String binPath = azureStorage.uploadFile(binFile, container, shop.getName() + "/products/bin/" + binFile.getOriginalFilename());
//
//        // Create product
//        Product product = new Product();
//        product.setShop(shop);
//        product.setName(productDto.getName());
//       // product.setGltfPath(gltfPath);
//       // product.setBinPath(binPath);
//        product.setTextures(productDto.getTextures());
//        product = productRepository.save(product);
//
//        // Create coordinate
//        Coordinate coordinate = new Coordinate();
//        coordinate.setShop(shop);
//        coordinate.setProduct(product);
//        coordinate.setXPos(productDto.getXPos());
//        coordinate.setYPos(productDto.getYPos());
//        coordinate.setZPos(productDto.getZPos());
//        coordinate.setXScale(productDto.getXScale());
//        coordinate.setYScale(productDto.getYScale());
//        coordinate.setZScale(productDto.getZScale());
//        coordinate.setXRot(productDto.getXRot());
//        coordinate.setYRot(productDto.getYRot());
//        coordinate.setZRot(productDto.getZRot());
//        coordinateRepository.save(coordinate);
//    }
//
//    public void grantShopAccess(Long ownerId, Long shopId, Long clientId, String accessLevel) {
//        Shops shop = shopRepository.findByIdAndOwnerId(shopId, ownerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Shop not found or not owned by you"));
//
//        User client = userRepository.findById(clientId)
//                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
//
//        // Implementation depends on your entity model (e.g., join table)
//    }
//}
