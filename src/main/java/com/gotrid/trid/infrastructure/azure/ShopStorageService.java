package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.common.exception.custom.UnAuthorizedException;
import com.gotrid.trid.common.exception.custom.shop.ShopNotFoundException;
import com.gotrid.trid.core.shop.model.Model;
import com.gotrid.trid.core.shop.model.Shop;
import com.gotrid.trid.core.shop.repository.ShopRepository;
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
                                 MultipartFile glbFile) {
        Shop shop = getShopById(shopId);

        if (!shop.getOwner().getId().equals(ownerId)) {
            throw new UnAuthorizedException("Unauthorized: You don't own this shop");
        }

        if (shop.getModel() == null) {
            shop.setModel(new Model());
        }

        String basePath = ownerId + "/" + shopId + "/";
        uploadAssets(shop.getModel(), CONTAINER_NAME, basePath, glbFile);
        shopRepository.save(shop);
    }

    public String getShopModelUrl(Integer shopId) {
        Shop shop = getShopById(shopId);
        return getModelUrl(shop.getModel(), CONTAINER_NAME);
    }

    public void deleteShopAssets(Integer shopId) {
        Shop shop = getShopById(shopId);
        deleteAssets(shop.getModel(), CONTAINER_NAME);
    }

    private Shop getShopById(Integer shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("Shop not found"));
    }
}