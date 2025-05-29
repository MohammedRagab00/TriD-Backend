package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.core.photo.model.Photo;
import com.gotrid.trid.core.threedModel.model.Model;
import com.gotrid.trid.core.threedModel.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class ModelStorageService extends AssetStorageService {
    private final ModelRepository modelRepository;

    @Value("${azure.storage.model-container-name}")
    private String CONTAINER_NAME;

    @Autowired
    public ModelStorageService(AzureStorageService azureStorageService,
                               ModelRepository modelRepository) {
        super(azureStorageService);
        this.modelRepository = modelRepository;
    }

    @Transactional
    public void uploadModelAsset(Model model, MultipartFile glbFile) {
        Integer modelId = model.getId();

        String basePath = modelId + "/";
        model.setGlb(uploadGlbModel(CONTAINER_NAME, basePath, glbFile));
        modelRepository.save(model);
    }

    @Transactional
    public void uploadModelPhotos(Model model, List<MultipartFile> photos) {
        Integer modelId = model.getId();

        for (MultipartFile photoFile : photos) {
            String basePath = modelId + "/photos/";

            Photo existingPhoto = model.getPhotos().stream()
                    .filter(p ->
                            p.getUrl().contains(Objects.requireNonNull(photoFile.getOriginalFilename()))
                    )
                    .findFirst()
                    .orElse(null);

            String newPhotoUrl = uploadPhoto(CONTAINER_NAME, basePath, photoFile);

            if (existingPhoto != null) {
                existingPhoto.setUrl(newPhotoUrl);
            } else {
                Photo newPhoto = new Photo();
                newPhoto.setUrl(newPhotoUrl);
                newPhoto.setModel(model);
                model.getPhotos().add(newPhoto);
            }
        }
        modelRepository.save(model);
    }

    public String getUrl(String filePath) {
        return getAssetUrl(CONTAINER_NAME, filePath);
    }

    public void deleteModelAssets(Model model) {
        deleteAsset(model.getGlb(), CONTAINER_NAME);

        model.getPhotos().forEach(photo -> deleteAsset(photo.getUrl(), CONTAINER_NAME));
    }
}
