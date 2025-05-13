package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.api.shop.dto.AssetUrlsDTO;
import com.gotrid.trid.core.shop.model.ModelAsset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.gotrid.trid.infrastructure.azure.AzureStorageService.ALLOWED_TYPES;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public abstract class AssetStorageService {
    protected final AzureStorageService azureStorageService;
    protected static final long MAX_SIZE = 300 * 1024 * 1024;

    protected void uploadAssets(ModelAsset modelAsset, String containerName, String basePath,
                                MultipartFile gltfFile, MultipartFile binFile,
                                MultipartFile iconFile, MultipartFile textureFile) {

        if (gltfFile != null && !gltfFile.isEmpty()) {
            String gltfFilename = gltfFile.getOriginalFilename();
            modelAsset.setGltf(
                    azureStorageService.uploadFile(gltfFile, containerName, basePath + gltfFilename, MAX_SIZE, List.of("model/gltf+json"))
            );
        }

        if (binFile != null && !binFile.isEmpty()) {
            String binFilename = binFile.getOriginalFilename();
            modelAsset.setBin(
                    azureStorageService.uploadFile(binFile, containerName, basePath + binFilename, MAX_SIZE, List.of("application/octet-stream"))
            );
        }

        if (iconFile != null && !iconFile.isEmpty()) {
            String iconFilename = iconFile.getOriginalFilename();
            modelAsset.setIcon(
                    azureStorageService.uploadFile(iconFile, containerName, basePath + iconFilename, MAX_SIZE, ALLOWED_TYPES)
            );
        }

        if (textureFile != null && !textureFile.isEmpty()) {
            String textureFilename = textureFile.getOriginalFilename();
            modelAsset.setTexture(
                    azureStorageService.uploadFile(textureFile, containerName, basePath + textureFilename, MAX_SIZE, ALLOWED_TYPES)
            );
        }
    }

    protected void deleteAssets(ModelAsset modelAsset, String containerName) {
        if (modelAsset != null) {
            if (modelAsset.getGltf() != null) {
                azureStorageService.deleteFile(modelAsset.getGltf(), containerName);
            }
            if (modelAsset.getBin() != null) {
                azureStorageService.deleteFile(modelAsset.getBin(), containerName);
            }
            if (modelAsset.getIcon() != null) {
                azureStorageService.deleteFile(modelAsset.getIcon(), containerName);
            }
            if (modelAsset.getTexture() != null) {
                azureStorageService.deleteFile(modelAsset.getTexture(), containerName);
            }
        }
    }

    protected AssetUrlsDTO getAssetUrls(ModelAsset modelAsset, String containerName) {
        if (modelAsset == null) {
            return new AssetUrlsDTO(null, null, null, null);
        }

        return new AssetUrlsDTO(
                modelAsset.getGltf() != null ? azureStorageService.getBlobUrlWithSas(containerName, modelAsset.getGltf()) : null,
                modelAsset.getBin() != null ? azureStorageService.getBlobUrlWithSas(containerName, modelAsset.getBin()) : null,
                modelAsset.getIcon() != null ? azureStorageService.getBlobUrlWithSas(containerName, modelAsset.getIcon()) : null,
                modelAsset.getTexture() != null ? azureStorageService.getBlobUrlWithSas(containerName, modelAsset.getTexture()) : null
        );
    }
}
