package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.exception.custom.UnAuthorizedException;
import com.gotrid.trid.exception.custom.product.ProductNotFoundException;
import com.gotrid.trid.shop.domain.product.Product;
import com.gotrid.trid.shop.dto.AssetUrlsDTO;
import com.gotrid.trid.shop.model.ModelAsset;
import com.gotrid.trid.shop.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ProductStorageService extends AssetStorageService {
    private final ProductRepository productRepository;

    @Value("${azure.storage.product-container-name}")
    private String CONTAINER_NAME;

    @Autowired
    public ProductStorageService(AzureStorageService azureStorageService, ProductRepository productRepository) {
        super(azureStorageService);
        this.productRepository = productRepository;
    }

    public void uploadProductAssets(Integer productId, Integer ownerId,
                                    MultipartFile gltfFile, MultipartFile binFile,
                                    MultipartFile iconFile, MultipartFile textureFile) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (!product.getShop().getOwner().getId().equals(ownerId)) {
            throw new UnAuthorizedException(
                    "Unauthorized: You don't own this product to be able to upload assets for it."
            );
        }

        if (product.getModelAsset() == null) {
            product.setModelAsset(new ModelAsset());
        }

        String basePath = product.getShop().getId() + "/" + productId + "/";
        uploadAssets(product.getModelAsset(), CONTAINER_NAME, basePath, gltfFile, binFile, iconFile, textureFile);
        productRepository.save(product);
    }

    public AssetUrlsDTO getProductAssetUrls(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return getAssetUrls(product.getModelAsset(), CONTAINER_NAME);
    }

    public void deleteProductAssets(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        deleteAssets(product.getModelAsset(), CONTAINER_NAME);
    }
}