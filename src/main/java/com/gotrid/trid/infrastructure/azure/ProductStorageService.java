package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.core.product.model.Product;
import com.gotrid.trid.core.product.repository.ProductRepository;
import com.gotrid.trid.core.threedModel.model.Model;
import jakarta.persistence.EntityNotFoundException;
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

        validateOwnership(ownerId, product.getShop().getOwner().getId());

        if (product.getModel() == null) {
            product.setModel(new Model());
        }

        String basePath = product.getShop().getId() + "/" + productId + "/";
        product.getModel().setGlb(uploadGlbModel(CONTAINER_NAME, basePath, glbFile));
        productRepository.save(product);
    }

    public String getProductModelUrl(Integer productId) {
        Product product = findProductById(productId);
        if (product.getModel() == null) {
            return null;
        }
        return getAssetUrl(CONTAINER_NAME, product.getModel().getGlb());
    }

    public void deleteProductAssets(Integer productId) {
        Product product = findProductById(productId);
        if (product.getModel() == null) {
            return;
        }
        deleteAsset(product.getModel().getGlb(), CONTAINER_NAME);
    }

    private Product findProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }
}