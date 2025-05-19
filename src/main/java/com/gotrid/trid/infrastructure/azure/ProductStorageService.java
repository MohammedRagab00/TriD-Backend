package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.common.exception.custom.UnAuthorizedException;
import com.gotrid.trid.common.exception.custom.product.ProductNotFoundException;
import com.gotrid.trid.core.shop.model.Model;
import com.gotrid.trid.core.shop.model.Product;
import com.gotrid.trid.core.shop.repository.ProductRepository;
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

    public void uploadProductAssets(Integer ownerId, Integer productId,
                                    MultipartFile glbFile) {
        Product product = findProductById(productId);

        if (!product.getShop().getOwner().getId().equals(ownerId)) {
            throw new UnAuthorizedException(
                    "Unauthorized: You don't own this product to be able to upload assets for it."
            );
        }

        if (product.getModel() == null) {
            product.setModel(new Model());
        }

        String basePath = product.getShop().getId() + "/" + productId + "/";
        uploadAssets(product.getModel(), CONTAINER_NAME, basePath, glbFile);
        productRepository.save(product);
    }

    public String getProductModelUrl(Integer productId) {
        Product product = findProductById(productId);
        return getModelUrl(product.getModel(), CONTAINER_NAME);
    }

    public void deleteProductAssets(Integer productId) {
        Product product = findProductById(productId);
        deleteAssets(product.getModel(), CONTAINER_NAME);
    }

    private Product findProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }
}