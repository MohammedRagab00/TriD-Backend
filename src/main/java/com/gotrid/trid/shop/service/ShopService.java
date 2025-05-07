package com.gotrid.trid.shop.service;

import com.gotrid.trid.exception.custom.UnAuthorizedException;
import com.gotrid.trid.exception.custom.shop.ShopException;
import com.gotrid.trid.exception.custom.shop.ShopNotFoundException;
import com.gotrid.trid.infrastructure.azure.ShopStorageService;
import com.gotrid.trid.infrastructure.common.PageResponse;
import com.gotrid.trid.shop.domain.Shop;
import com.gotrid.trid.shop.dto.*;
import com.gotrid.trid.shop.mapper.CoordinateMapper;
import com.gotrid.trid.shop.mapper.ShopMapper;
import com.gotrid.trid.shop.mapper.SocialMapper;
import com.gotrid.trid.shop.model.Coordinates;
import com.gotrid.trid.shop.model.ModelAsset;
import com.gotrid.trid.shop.model.Social;
import com.gotrid.trid.shop.repository.ShopRepository;
import com.gotrid.trid.user.domain.Users;
import com.gotrid.trid.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.gotrid.trid.exception.handler.BusinessErrorCode.SHOP_NAME_EXISTS;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class ShopService {
    private final UserRepository userRepository;
    private final ShopStorageService shopStorageService;
    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;
    private final SocialMapper socialMapper;
    private final CoordinateMapper coordinateMapper;

    @Transactional
    public Integer createShop(Integer ownerId, ShopRequest req) {
        if (shopRepository.existsByNameIgnoreCase(req.name())) {
            throw new ShopException(
                    SHOP_NAME_EXISTS,
                    "Shop with name '" + req.name() + "' already exists"
            );
        }

        Users owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Shop shop = shopMapper.toEntity(req);
        shop.setOwner(owner);

        return shopRepository.saveAndFlush(shop).getId();
    }

    @Transactional
    public void updateShopSocial(Integer ownerId, Integer shopId, SocialDTO socialDTO) {
        Shop shop = findShopById(shopId);
        validateOwnership(shop, ownerId);

        Social social = shop.getSocials().stream()
                .filter(s -> s.getPlatform().equals(socialDTO.platform()))
                .findFirst()
                .map(s -> socialMapper.updateExisting(s, socialDTO))
                .orElseGet(() -> socialMapper.toEntity(socialDTO, shop));

        shop.getSocials().add(social);

        shopRepository.save(shop);
    }

    @Transactional
    public void updateShop(Integer ownerId, Integer shopId, ShopRequest dto) {
        Shop shop = findShopById(shopId);
        validateOwnership(shop, ownerId);
        validateShopName(shop, dto.name());

        updateShop(shop, dto);

        shopRepository.save(shop);
    }

    @Transactional
    public void updateShopCoordinates(Integer ownerId, Integer shopId, CoordinateDTO coordinates) {
        Shop shop = findShopById(shopId);
        validateOwnership(shop, ownerId);

        ModelAsset modelAsset = shop.getModelAsset() != null ?
                shop.getModelAsset() : new ModelAsset();

        Coordinates updatedCoordinates = modelAsset.getCoordinates() != null ?
                modelAsset.getCoordinates() :
                new Coordinates();

        updateCoordinates(updatedCoordinates, coordinates);

        modelAsset.setCoordinates(updatedCoordinates);
        shop.setModelAsset(modelAsset);

        shopRepository.save(shop);
    }

    public ShopAssetsDTO getShopAssetDetails(Integer shopId) {
        Shop shop = findShopById(shopId);

        AssetUrlsDTO urls = shopStorageService.getShopAssetUrls(shopId);
        CoordinateDTO coordinates = shop.getModelAsset() != null && shop.getModelAsset().getCoordinates() != null ?
                coordinateMapper.toDTO(shop.getModelAsset().getCoordinates()) : null;

        return new ShopAssetsDTO(urls, coordinates);
    }

    @Transactional(readOnly = true)
    public ShopResponse getShop(Integer shopId) {
        Shop shop = findShopById(shopId);
        return shopMapper.toResponse(shop);
    }

    @Transactional(readOnly = true)
    public PageResponse<ShopResponse> getAllShops(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Shop> shopsPage = shopRepository.findAll(pageable);

        List<ShopResponse> shopResponses = shopsPage.stream()
                .map(shopMapper::toResponse)
                .toList();

        return new PageResponse<>(
                shopResponses,
                shopsPage.getNumber(),
                shopsPage.getSize(),
                shopsPage.getTotalElements(),
                shopsPage.getTotalPages(),
                shopsPage.isFirst(),
                shopsPage.isLast()
        );
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
        if (!shop.getName().equalsIgnoreCase(newName) &&
                shopRepository.existsByNameIgnoreCase(newName)) {
            throw new ShopException(
                    SHOP_NAME_EXISTS,
                    "Shop with name '" + newName + "' already exists"
            );
        }
    }

    private void updateShop(Shop shop, ShopRequest updateDTO) {
        shop.setName(updateDTO.name());
        shop.setCategory(updateDTO.category());
        shop.setLocation(updateDTO.location());
        shop.setDescription(updateDTO.description());
        shop.setEmail(updateDTO.email());
        shop.setPhone(updateDTO.phone());
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