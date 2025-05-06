package com.gotrid.trid.shop.service;

import com.gotrid.trid.exception.custom.UnAuthorizedException;
import com.gotrid.trid.exception.custom.shop.ShopNotFoundException;
import com.gotrid.trid.infrastructure.azure.ShopStorageService;
import com.gotrid.trid.shop.domain.Shop;
import com.gotrid.trid.shop.domain.ShopDetail;
import com.gotrid.trid.shop.dto.DetailsShopDTO;
import com.gotrid.trid.shop.mappers.ShopDetailMapper;
import com.gotrid.trid.shop.model.AssetInfo;
import com.gotrid.trid.shop.model.Coordinates;
import com.gotrid.trid.shop.repository.ShopRepository;
import com.gotrid.trid.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class ShopService {
    private final ShopRepository shopRepository;
    private final ShopStorageService shopStorageService;
    private final ShopDetailMapper shopDetailMapper;

    @Transactional
    public void createShop(Integer ownerId, DetailsShopDTO detailsShopDTO) {
        ShopDetail shopDetail = shopDetailMapper.toEntity(detailsShopDTO);

        Shop shop = Shop.builder()
                .shopDetail(shopDetail)
                .owner(Users.builder().id(ownerId).build())
                .build();

        shopRepository.save(shop);
    }

    public void uploadShopAssets(
            Integer ownerId,
            Integer shopId,
            MultipartFile gltfFile,
            MultipartFile binFile,
            MultipartFile iconFile,
            MultipartFile textureFile
    ) {
        shopStorageService.uploadShopAssets(ownerId, shopId, gltfFile, binFile, iconFile, textureFile);
    }

    @Transactional
    public void updateShopCoordinates(Integer ownerId, Integer shopId, Coordinates coordinates) {
        Shop shop = findShop(shopId);

        if (!shop.getOwner().getId().equals(ownerId)) {
            throw new UnAuthorizedException("You are not authorized to update this shop");
        }

        AssetInfo assetInfo = shop.getAssetInfo() != null ?
                shop.getAssetInfo() :
                AssetInfo.builder().build();

        Coordinates updatedCoordinates = assetInfo.getCoordinates() != null ?
                assetInfo.getCoordinates() :
                Coordinates.builder().build();

        BeanUtils.copyProperties(coordinates, updatedCoordinates, "id", "createdDate", "lastModifiedDate");

        assetInfo.setCoordinates(updatedCoordinates);
        shop.setAssetInfo(assetInfo);

        shopRepository.save(shop);
    }

    private Shop findShop(Integer shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("Shop not found"));
    }
}