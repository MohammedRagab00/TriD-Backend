package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.common.exception.custom.shop.ShopNotFoundException;
import com.gotrid.trid.core.photo.model.Photo;
import com.gotrid.trid.core.shop.model.Shop;
import com.gotrid.trid.core.shop.repository.ShopRepository;
import com.gotrid.trid.core.threedModel.model.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.gotrid.trid.infrastructure.azure.AzureStorageService.ALLOWED_TYPES;

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

    public void uploadShopLogo(Integer ownerId, Integer shopId, MultipartFile logoFile) {
        Shop shop = getShopById(shopId);
        validateOwnership(ownerId, shop.getOwner().getId());

        String basePath = ownerId + "/" + shopId + "/";

        shop.setLogo(uploadLogo(CONTAINER_NAME, basePath, logoFile));
        shopRepository.save(shop);

    }

    @Transactional
    public void uploadShopPhotos(Integer ownerId, Integer shopId, List<MultipartFile> photoFiles) {
        Shop shop = getShopById(shopId);
        validateOwnership(ownerId, shop.getOwner().getId());

        for (MultipartFile photoFile : photoFiles) {
            String basePath = ownerId + "/" + shopId + "/photos/";

            //* Check if the photo already exists
            Photo existingPhoto = shop.getModel().getPhotos().stream()
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
                newPhoto.setModel(shop.getModel());
                shop.getModel().getPhotos().add(newPhoto);
            }
        }

        shopRepository.save(shop);
    }

    public void uploadShopAssets(Integer ownerId, Integer shopId,
                                 MultipartFile glbFile) {
        Shop shop = getShopById(shopId);

        validateOwnership(ownerId, shop.getOwner().getId());

        if (shop.getModel() == null) {
            shop.setModel(new Model());
        }

        String basePath = ownerId + "/" + shopId + "/";
        shop.getModel().setGlb(uploadGlbModel(CONTAINER_NAME, basePath, glbFile));
        shopRepository.save(shop);
    }

    public String getShopModelUrl(Integer shopId) {
        Shop shop = getShopById(shopId);
        if (shop.getModel() == null) {
            return null;
        }
        return getAssetUrl(CONTAINER_NAME, shop.getModel().getGlb());
    }

    public String getPhotoUrl(String filePath) {
        return getAssetUrl(CONTAINER_NAME, filePath);
    }

    public void deleteShopAssets(Shop shop) {
        if (shop.getModel() != null) {
            deleteAsset(shop.getModel().getGlb(), CONTAINER_NAME);
        }
        deleteAsset(shop.getLogo(), CONTAINER_NAME);
        shop.getModel().getPhotos().forEach(photo -> deleteAsset(photo.getUrl(), CONTAINER_NAME));
    }

    private String uploadLogo(String containerName, String basePath,
                              MultipartFile file) {

        if (file != null && !file.isEmpty()) {
            String logoFilename = "logoFile" + azureStorageService.getFileExtension(file);
            return azureStorageService.uploadFile(file, containerName, basePath + logoFilename, MAX_SIZE, ALLOWED_TYPES);
        }
        return null;
    }

    private Shop getShopById(Integer shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("Shop not found"));
    }
}