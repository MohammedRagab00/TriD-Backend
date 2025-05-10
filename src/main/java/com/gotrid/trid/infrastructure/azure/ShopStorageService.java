package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.exception.custom.UnAuthorizedException;
import com.gotrid.trid.exception.custom.shop.ShopNotFoundException;
import com.gotrid.trid.shop.domain.Shop;
import com.gotrid.trid.shop.dto.AssetUrlsDTO;
import com.gotrid.trid.shop.model.ModelAsset;
import com.gotrid.trid.shop.repository.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ShopStorageService extends AssetStorageService {
    private final ShopRepository shopRepository;

    @Value("${azure.storage.shop-container-name}")
    private String CONTAINER_NAME;

    @Autowired
    public ShopStorageService(AzureStorageService azureStorageService, ShopRepository shopRepository) {
        super(azureStorageService);
        this.shopRepository = shopRepository;
    }

    public void uploadShopAssets(Integer ownerId, Integer shopId,
                                 MultipartFile gltfFile, MultipartFile binFile,
                                 MultipartFile iconFile, MultipartFile textureFile) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("Shop not found"));

        if (!shop.getOwner().getId().equals(ownerId)) {
            throw new UnAuthorizedException("Unauthorized: You don't own this shop");
        }

        if (shop.getModelAsset() == null) {
            shop.setModelAsset(new ModelAsset());
        }

        String basePath = ownerId + "/" + shopId + "/";
        uploadAssets(shop.getModelAsset(), CONTAINER_NAME, basePath, gltfFile, binFile, iconFile, textureFile);
        shopRepository.save(shop);
    }

    public AssetUrlsDTO getShopAssetUrls(Integer shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("Shop not found"));
        return getAssetUrls(shop.getModelAsset(), CONTAINER_NAME);
    }

    public void deleteShopAssets(Integer shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("Shop not found"));
        deleteAssets(shop.getModelAsset(), CONTAINER_NAME);
    }
}