package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.exception.custom.UnAuthorizedException;
import com.gotrid.trid.exception.custom.shop.ShopNotFoundException;
import com.gotrid.trid.shop.domain.Shop;
import com.gotrid.trid.shop.dto.AssetUrlsDTO;
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

        if (shop.getAssetInfo() == null) {
            shop.setAssetInfo(new AssetInfo());
        }

        if (gltfFile != null && !gltfFile.isEmpty()) {
            if (shop.getAssetInfo().getGltf() != null) {
                azureStorageService.deleteFile(shop.getAssetInfo().getGltf(), CONTAINER_NAME);
            }
            String gltfFilename = "gltfFile" + azureStorageService.getFileExtension(gltfFile);
            shop.getAssetInfo().setGltf(
                    azureStorageService.uploadFile(gltfFile, CONTAINER_NAME, basePath + gltfFilename, MAX_SIZE, List.of("model/gltf+json"))
            );
        }

        if (binFile != null && !binFile.isEmpty()) {
            if (shop.getAssetInfo().getBin() != null) {
                azureStorageService.deleteFile(shop.getAssetInfo().getBin(), CONTAINER_NAME);
            }
            String binFilename = "binFile" + azureStorageService.getFileExtension(binFile);
            shop.getAssetInfo().setBin(
                    azureStorageService.uploadFile(binFile, CONTAINER_NAME, basePath + binFilename, MAX_SIZE, List.of("application/octet-stream"))
            );
        }

        if (iconFile != null && !iconFile.isEmpty()) {
            if (shop.getAssetInfo().getIcon() != null) {
                azureStorageService.deleteFile(shop.getAssetInfo().getIcon(), CONTAINER_NAME);
            }
            String iconFilename = "iconFile" + azureStorageService.getFileExtension(iconFile);
            shop.getAssetInfo().setIcon(
                    azureStorageService.uploadFile(iconFile, CONTAINER_NAME, basePath + iconFilename, MAX_SIZE, ALLOWED_TYPES)
            );
        }

        if (textureFile != null && !textureFile.isEmpty()) {
            if (shop.getAssetInfo().getTexture() != null) {
                azureStorageService.deleteFile(shop.getAssetInfo().getTexture(), CONTAINER_NAME);
            }
            String textureFilename = "textureFile" + azureStorageService.getFileExtension(textureFile);
            shop.getAssetInfo().setTexture(
                    azureStorageService.uploadFile(textureFile, CONTAINER_NAME, basePath + textureFilename, MAX_SIZE, ALLOWED_TYPES)
            );
        }

        shopRepository.save(shop);
    }

    public AssetUrlsDTO getShopAssetUrls(Integer shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("Shop not found"));

        AssetInfo assetInfo = shop.getAssetInfo();
        if (assetInfo == null) {
            return new AssetUrlsDTO(null, null, null, null);
        }

        return new AssetUrlsDTO(
                assetInfo.getGltf() != null ? azureStorageService.getBlobUrlWithSas(CONTAINER_NAME, assetInfo.getGltf()) : null,
                assetInfo.getBin() != null ? azureStorageService.getBlobUrlWithSas(CONTAINER_NAME, assetInfo.getBin()) : null,
                assetInfo.getIcon() != null ? azureStorageService.getBlobUrlWithSas(CONTAINER_NAME, assetInfo.getIcon()) : null,
                assetInfo.getTexture() != null ? azureStorageService.getBlobUrlWithSas(CONTAINER_NAME, assetInfo.getTexture()) : null
        );
    }
}