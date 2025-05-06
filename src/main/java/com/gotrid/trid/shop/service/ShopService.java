package com.gotrid.trid.shop.service;

import com.gotrid.trid.exception.custom.UnAuthorizedException;
import com.gotrid.trid.exception.custom.shop.ShopException;
import com.gotrid.trid.exception.custom.shop.ShopNotFoundException;
import com.gotrid.trid.infrastructure.azure.ShopStorageService;
import com.gotrid.trid.shop.domain.Shop;
import com.gotrid.trid.shop.domain.ShopDetail;
import com.gotrid.trid.shop.dto.*;
import com.gotrid.trid.shop.mapper.CoordinateMapper;
import com.gotrid.trid.shop.mapper.ShopDetailMapper;
import com.gotrid.trid.shop.mapper.SocialMapper;
import com.gotrid.trid.shop.model.AssetInfo;
import com.gotrid.trid.shop.model.Coordinates;
import com.gotrid.trid.shop.model.Social;
import com.gotrid.trid.shop.repository.ShopDetailRepository;
import com.gotrid.trid.shop.repository.ShopRepository;
import com.gotrid.trid.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gotrid.trid.exception.handler.BusinessErrorCode.SHOP_NAME_EXISTS;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class ShopService {
    private final ShopRepository shopRepository;
    private final ShopDetailRepository shopDetailRepository;
    private final ShopDetailMapper shopDetailMapper;
    private final ShopStorageService shopStorageService;
    private final CoordinateMapper coordinateMapper;
    private final SocialMapper socialMapper;

    @Transactional
    public Integer createShop(Integer ownerId, ShopDetailDTO dto) {
        if (shopDetailRepository.existsByNameIgnoreCase(dto.name())) {
            throw new ShopException(
                    SHOP_NAME_EXISTS,
                    "Shop with name '" + dto.name() + "' already exists"
            );
        }

        ShopDetail shopDetail = shopDetailMapper.toEntity(dto);

        Shop shop = Shop.builder()
                .shopDetail(shopDetail)
                .owner(Users.builder().id(ownerId).build())
                .build();

        return shopRepository.saveAndFlush(shop).getId();
    }

    @Transactional(readOnly = true)
    public ShopDetailResponse getShop(Integer shopId) {
        Shop shop = findShopById(shopId);
        return shopDetailMapper.toResponse(shop.getShopDetail());
    }

    @Transactional
    public void updateShopSocial(Integer ownerId, Integer shopId, SocialDTO socialDTO) {
        Shop shop = findShopById(shopId);
        validateOwnership(shop, ownerId);

        ShopDetail shopDetail = shop.getShopDetail();

        Social social = shopDetail.getSocials().stream()
                .filter(s -> s.getPlatform().equals(socialDTO.platform()))
                .findFirst()
                .map(s -> socialMapper.updateExisting(s, socialDTO))
                .orElseGet(() -> socialMapper.toEntity(socialDTO, shopDetail));

        shopDetail.getSocials().add(social);

        shopRepository.save(shop);
    }

    @Transactional
    public void updateShop(Integer ownerId, Integer shopId, ShopDetailDTO dto) {
        Shop shop = findShopById(shopId);
        validateOwnership(shop, ownerId);
        validateShopName(shop, dto.name());

        ShopDetail shopDetail = shop.getShopDetail();
        updateShopDetails(shopDetail, dto);

        shopRepository.save(shop);
    }

    public ShopAssetsDTO getShopAssetDetails(Integer shopId) {
        Shop shop = findShopById(shopId);

        AssetUrlsDTO urls = shopStorageService.getShopAssetUrls(shopId);
        CoordinateDTO coordinates = shop.getAssetInfo() != null && shop.getAssetInfo().getCoordinates() != null ?
                coordinateMapper.toDTO(shop.getAssetInfo().getCoordinates()) : null;

        return new ShopAssetsDTO(urls, coordinates);
    }

    @Transactional
    public void updateShopCoordinates(Integer ownerId, Integer shopId, CoordinateDTO coordinates) {
        Shop shop = findShopById(shopId);

        if (!shop.getOwner().getId().equals(ownerId)) {
            throw new UnAuthorizedException("You are not authorized to update this shop");
        }

        AssetInfo assetInfo = shop.getAssetInfo() != null ?
                shop.getAssetInfo() :
                AssetInfo.builder().build();

        Coordinates updatedCoordinates = assetInfo.getCoordinates() != null ?
                assetInfo.getCoordinates() :
                Coordinates.builder().build();

        updateCoordinates(updatedCoordinates, coordinates);

        assetInfo.setCoordinates(updatedCoordinates);
        shop.setAssetInfo(assetInfo);

        shopRepository.save(shop);
    }


    //* * * * Helper methods * * * *//

    private Shop findShopById(Integer shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("Shop not found"));
    }

    private void validateOwnership(Shop shop, Integer ownerId) {
        if (!shop.getOwner().getId().equals(ownerId)) {
            throw new UnAuthorizedException("You are not authorized to update this shop");
        }
    }

    private void validateShopName(Shop shop, String newName) {
        if (!shop.getShopDetail().getName().equalsIgnoreCase(newName) &&
                shopDetailRepository.existsByNameIgnoreCase(newName)) {
            throw new ShopException(
                    SHOP_NAME_EXISTS,
                    "Shop with name '" + newName + "' already exists"
            );
        }
    }

    private void updateShopDetails(ShopDetail shopDetail, ShopDetailDTO updateDTO) {
        shopDetail.setName(updateDTO.name());
        shopDetail.setCategory(updateDTO.category());
        shopDetail.setLocation(updateDTO.location());
        shopDetail.setDescription(updateDTO.description());
        shopDetail.setEmail(updateDTO.email());
        shopDetail.setPhone(updateDTO.phone());
    }

    private void updateCoordinates(Coordinates coordinates, CoordinateDTO dto) {
        coordinates.setX_pos(dto.x_pos());
        coordinates.setY_pos(dto.y_pos());
        coordinates.setZ_pos(dto.z_pos());
        coordinates.setX_scale(dto.x_scale());
        coordinates.setY_scale(dto.y_scale());
        coordinates.setZ_scale(dto.z_scale());
        coordinates.setX_rot(dto.x_rot());
        coordinates.setY_rot(dto.y_rot());
        coordinates.setZ_rot(dto.z_rot());
    }
}