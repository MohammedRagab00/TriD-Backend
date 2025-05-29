package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.common.exception.custom.UnAuthorizedException;
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

    protected String uploadGlbModel(String containerName, String basePath,
                                    MultipartFile glbFile) {

        if (glbFile != null && !glbFile.isEmpty()) {
            String gltfFilename = "glbFile" + azureStorageService.getFileExtension(glbFile);
            return azureStorageService.uploadFile(glbFile, containerName, basePath + gltfFilename, MAX_SIZE, List.of("model/gltf-binary"));
        }
        return null;
    }

    protected String uploadPhoto(String containerName, String basePath,
                              MultipartFile file) {

        if (file != null && !file.isEmpty()) {
            return azureStorageService.uploadFile(file, containerName, basePath + file.getOriginalFilename(), MAX_SIZE, ALLOWED_TYPES);
        }
        return null;
    }

    protected void deleteAsset(String assetUrl, String containerName) {
        if (assetUrl != null) {
            azureStorageService.deleteFile(assetUrl, containerName);
        }
    }

    protected String getAssetUrl(String containerName, String assetUrl) {
        return assetUrl != null ? azureStorageService.getBlobUrlWithSas(containerName, assetUrl) : null;
    }

    protected void validateOwnership(Integer ownerId, Integer thingRealOwnerId) {
        if (!ownerId.equals(thingRealOwnerId)) {
            throw new UnAuthorizedException("Unauthorized: You don't own this");
        }
    }
}
