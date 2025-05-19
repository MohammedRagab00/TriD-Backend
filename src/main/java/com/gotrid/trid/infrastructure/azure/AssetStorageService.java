package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.core.shop.model.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public abstract class AssetStorageService {
    protected final AzureStorageService azureStorageService;
    protected static final long MAX_SIZE = 300 * 1024 * 1024;

    protected void uploadAssets(Model model, String containerName, String basePath,
                                MultipartFile glbFile) {

        if (glbFile != null && !glbFile.isEmpty()) {
            String gltfFilename = glbFile.getOriginalFilename();
            model.setGlb(
                    azureStorageService.uploadFile(glbFile, containerName, basePath + gltfFilename, MAX_SIZE, List.of("model/gltf-binary"))
            );
        }
    }

    protected void deleteAssets(Model model, String containerName) {
        if (model != null) {
            if (model.getGlb() != null) {
                azureStorageService.deleteFile(model.getGlb(), containerName);
            }
            //todo: delete other assets if needed (icon, texture for shop)
        }
    }

    protected String getModelUrl(Model model, String containerName) {
        if (model == null) return null;

        return model.getGlb() != null ? azureStorageService.getBlobUrlWithSas(containerName, model.getGlb()) : null;
    }
}
