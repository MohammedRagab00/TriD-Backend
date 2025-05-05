package com.gotrid.trid.marshmello.services;//package com.example.metamall.services;
//
//import com.example.metamall.models.Shops.Product;
//import com.example.metamall.models.Shops.Shop;
//import com.example.metamall.repository.Shop.CoordinatesRepository;
//import com.example.metamall.repository.Shop.DetailsProductRepository;
//import com.example.metamall.repository.Shop.ProductRepository;
//import com.example.metamall.repository.Shop.ShopRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Service
//@Transactional
//public class ProductService {
//
//    private final ProductRepository productRepository;
//    private final AzureBlobService azureBlobService;
//    private final DetailsProductRepository detailsProductRepository;
//    private final CoordinatesRepository coordinateRepository;
//    private final ShopRepository shopRepository;
//
//    public ProductService(ProductRepository productRepository,
//                          AzureBlobService azureBlobService,
//                          DetailsProductRepository detailsProductRepository,
//                          CoordinatesRepository coordinateRepository,
//                          ShopRepository shopRepository) {
//        this.productRepository = productRepository;
//        this.azureBlobService = azureBlobService;
//        this.detailsProductRepository = detailsProductRepository;
//        this.coordinateRepository = coordinateRepository;
//        this.shopRepository = shopRepository;
//    }
//
//    public Product createProductWithAssets(Long shopId, String userId, String productId,
//                                           Product product,
//                                           MultipartFile gltfFile,
//                                           MultipartFile binFile,
//                                           MultipartFile iconFile,
//                                           MultipartFile textureFile) throws IOException {
//
//        // Get shop to build the path
//        Shop shop = shopRepository.findById(shopId)
//                .orElseThrow(() -> new RuntimeException("Shop not found"));
//
//        // Save coordinate if new
//        if (product.getCoordinates() != null && product.getCoordinates().getCoordinatesId() == null) {
//            coordinateRepository.save(product.getCoordinates());
//        }
//
//        // Save product details
//        if (product.getDetailsProduct() != null) {
//            detailsProductRepository.save(product.getDetailsProduct());
//        }
//
//        // Upload assets to Azure Blob Storage
//        String basePath = "shops/" + userId + "/" + shop.getDetailsShop().getName() +
//                "/products/" + productId + "/";
//
//        if (gltfFile != null && !gltfFile.isEmpty()) {
//            String gltfPath = basePath + "gltfFile";
//            product.setProductGltf(azureBlobService.uploadFile(gltfPath, gltfFile));
//        }
//
//        if (binFile != null && !binFile.isEmpty()) {
//            String binPath = basePath + "binFile";
//            product.setProductBin(azureBlobService.uploadFile(binPath, binFile));
//        }
//
//        if (iconFile != null && !iconFile.isEmpty()) {
//            String iconPath = basePath + "icon";
//            product.setProductIcon(azureBlobService.uploadFile(iconPath, iconFile));
//        }
//
//        if (textureFile != null && !textureFile.isEmpty()) {
//            String texturePath = basePath + "texture";
//            product.setTexture(azureBlobService.uploadFile(texturePath, textureFile));
//        }
//
//        // Save the product
//        return productRepository.save(product);
//    }
//
//    public byte[] getProductGltfFile(Long productId) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        return azureBlobService.getFile(product.getProductGltf());
//    }
//
//    public byte[] getProductBinFile(Long productId) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        return azureBlobService.getFile(product.getProductBin());
//    }
//
//    public byte[] getProductIcon(Long productId) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        return azureBlobService.getFile(product.getProductIcon());
//    }
//
//    public byte[] getProductTexture(Long productId) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        return azureBlobService.getFile(product.getTexture());
//    }
//
//    public void deleteProductWithAssets(Long productId) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        // Delete assets from Azure Blob Storage
//        if (product.getProductGltf() != null) {
//            azureBlobService.deleteFile(product.getProductGltf());
//        }
//        if (product.getProductBin() != null) {
//            azureBlobService.deleteFile(product.getProductBin());
//        }
//        if (product.getProductIcon() != null) {
//            azureBlobService.deleteFile(product.getProductIcon());
//        }
//        if (product.getTexture() != null) {
//            azureBlobService.deleteFile(product.getTexture());
//        }
//
//        // Delete from database
//        productRepository.delete(product);
//    }
//}
