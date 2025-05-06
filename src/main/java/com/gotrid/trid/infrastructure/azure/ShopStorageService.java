package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.exception.custom.UnAuthorizedException;
import com.gotrid.trid.exception.custom.shop.ShopNotFoundException;
import com.gotrid.trid.shop.domain.Shop;
import com.gotrid.trid.shop.model.AssetInfo;
import com.gotrid.trid.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.gotrid.trid.infrastructure.azure.AzureStorageService.ALLOWED_TYPES;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ShopStorageService {
    private final AzureStorageService azureStorageService;
    private final ShopRepository shopRepository;

    private static final long MAX_SIZE = 300 * 1024 * 1024;

    @Value("${azure.storage.shop-container-name}")
    private String CONTAINER_NAME;

    public void uploadShopAssets(Integer ownerId, Integer shopId,
                                 MultipartFile gltfFile, MultipartFile binFile,
                                 MultipartFile iconFile, MultipartFile textureFile) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("Shop not found"));

        if (!shop.getOwner().getId().equals(ownerId)) {
            throw new UnAuthorizedException("Unauthorized: You don't own this shop");
        }

        String basePath = ownerId + "/" + shopId + "/";

        String binFilename = "binFile" + azureStorageService.getFileExtension(binFile),
                gltfFilename = "gltfFile" + azureStorageService.getFileExtension(gltfFile),
                iconFilename = "iconFile" + azureStorageService.getFileExtension(iconFile),
                textureFilename = "textureFile" + azureStorageService.getFileExtension(textureFile);

        if (shop.getAssetInfo() == null) {
            shop.setAssetInfo(new AssetInfo());
        }

        if (!gltfFile.isEmpty()) {
            if (shop.getAssetInfo().getGltf() != null) {
                azureStorageService.deleteFile(shop.getAssetInfo().getGltf(), CONTAINER_NAME);
            }
            shop.getAssetInfo().setGltf(
                    azureStorageService.uploadFile(gltfFile, CONTAINER_NAME, basePath + gltfFilename, MAX_SIZE, List.of("model/gltf+json"))
            );
        }

        if (!binFile.isEmpty()) {
            if (shop.getAssetInfo().getBin() != null) {
                azureStorageService.deleteFile(shop.getAssetInfo().getBin(), CONTAINER_NAME);
            }
            shop.getAssetInfo().setBin(
                    azureStorageService.uploadFile(binFile, CONTAINER_NAME, basePath + binFilename, MAX_SIZE, List.of("application/octet-stream"))
            );
        }

        if (!iconFile.isEmpty()) {
            if (shop.getAssetInfo().getIcon() != null) {
                azureStorageService.deleteFile(shop.getAssetInfo().getIcon(), CONTAINER_NAME);
            }
            shop.getAssetInfo().setIcon(
                    azureStorageService.uploadFile(iconFile, CONTAINER_NAME, basePath + iconFilename, MAX_SIZE, ALLOWED_TYPES)
            );
        }

        if (!textureFile.isEmpty()) {
            if (shop.getAssetInfo().getTexture() != null) {
                azureStorageService.deleteFile(shop.getAssetInfo().getTexture(), CONTAINER_NAME);
            }
            shop.getAssetInfo().setTexture(
                    azureStorageService.uploadFile(textureFile, CONTAINER_NAME, basePath + textureFilename, MAX_SIZE, ALLOWED_TYPES)
            );
        }

        shopRepository.save(shop);
    }
}