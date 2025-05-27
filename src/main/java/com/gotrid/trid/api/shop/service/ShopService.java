package com.gotrid.trid.api.shop.service;

import com.gotrid.trid.api.product.service.ProductService;
import com.gotrid.trid.api.shop.dto.*;
import com.gotrid.trid.common.exception.custom.shop.ShopException;
import com.gotrid.trid.common.exception.custom.shop.ShopNotFoundException;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.core.product.model.Product;
import com.gotrid.trid.core.shop.mapper.ShopMapper;
import com.gotrid.trid.core.shop.mapper.SocialMapper;
import com.gotrid.trid.core.shop.model.Shop;
import com.gotrid.trid.core.shop.model.Social;
import com.gotrid.trid.core.shop.repository.ShopRepository;
import com.gotrid.trid.core.threedModel.dto.CoordinateDTO;
import com.gotrid.trid.core.threedModel.mapper.CoordinateMapper;
import com.gotrid.trid.core.threedModel.model.Coordinates;
import com.gotrid.trid.core.threedModel.model.Model;
import com.gotrid.trid.core.user.model.Role;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.core.user.repository.UserRepository;
import com.gotrid.trid.infrastructure.azure.ShopStorageService;
import com.gotrid.trid.infrastructure.service.BaseModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gotrid.trid.common.exception.handler.BusinessErrorCode.SHOP_NAME_EXISTS;

@Service
public class ShopService extends BaseModelService {
    private final UserRepository userRepository;
    private final ShopStorageService shopStorageService;
    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;
    private final SocialMapper socialMapper;
    private final ProductService productService;

    @Autowired
    public ShopService(CoordinateMapper coordinateMapper,
                       UserRepository userRepository,
                       ShopStorageService shopStorageService,
                       ShopRepository shopRepository,
                       ShopMapper shopMapper,
                       SocialMapper socialMapper,
                       ProductService productService) {
        super(coordinateMapper);
        this.userRepository = userRepository;
        this.shopStorageService = shopStorageService;
        this.shopRepository = shopRepository;
        this.shopMapper = shopMapper;
        this.socialMapper = socialMapper;
        this.productService = productService;
    }

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
        validateOwnership(ownerId, shop.getOwner().getId(), "You are not authorized to update this shop");

        Social social = shop.getSocials().stream()
                .filter(s -> s.getPlatform().equals(socialDTO.platform()))
                .findFirst()
                .map(s -> socialMapper.updateExisting(s, socialDTO))
                .orElseGet(() -> socialMapper.toEntity(socialDTO, shop));

        shop.getSocials().add(social);
        shopRepository.save(shop);
    }

    @Transactional
    public void updateShopCoordinates(Integer ownerId, Integer shopId, CoordinateDTO coordinates) {
        Shop shop = findShopById(shopId);
        validateOwnership(ownerId, shop.getOwner().getId(), "You are not authorized to update this shop");

        Model model = getOrCreateModelAsset(shop.getModel());
        Coordinates updatedCoordinates = getOrCreateCoordinates(model);

        updateCoordinates(updatedCoordinates, coordinates);
        model.setCoordinates(updatedCoordinates);
        shop.setModel(model);

        shopRepository.save(shop);
    }

    @Transactional(readOnly = true)
    public ShopModelResponse getShopModelDetails(Integer shopId) {
        Shop shop = findShopById(shopId);

        String glbUrl = shopStorageService.getShopModelUrl(shopId);

        List<String> imageUrls = shop.getPhotos().stream()
                .map(photo -> shopStorageService.getPhotoUrl(photo.getUrl()))
                .toList();

        return new ShopModelResponse(createModelDetails(shop.getModel(), glbUrl), imageUrls);
    }

    @Transactional
    public void patchShop(Integer ownerId, Integer shopId, ShopUpdateRequest shopUpdateRequest) {
        Shop shop = findShopById(shopId);
        validateOwnership(ownerId, shop.getOwner().getId(), "Unauthorized: You don't own this shop");

        if (shopUpdateRequest.getName() != null) {
            validateShopName(shop, shopUpdateRequest.getName());
            shop.setName(shopUpdateRequest.getName());
        }
        if (shopUpdateRequest.getCategory() != null) shop.setCategory(shopUpdateRequest.getCategory());
        if (shopUpdateRequest.getLocation() != null) shop.setLocation(shopUpdateRequest.getLocation());
        if (shopUpdateRequest.getDescription() != null) shop.setDescription(shopUpdateRequest.getDescription());
        if (shopUpdateRequest.getEmail() != null) shop.setEmail(shopUpdateRequest.getEmail());
        if (shopUpdateRequest.getPhone() != null) shop.setPhone(shopUpdateRequest.getPhone());

        // Handle file uploads
        if (shopUpdateRequest.getLogo() != null) {
            shopStorageService.uploadShopLogo(ownerId, shopId, shopUpdateRequest.getLogo());
        }
        if (shopUpdateRequest.getGlb() != null) {
            shopStorageService.uploadShopAssets(ownerId, shopId, shopUpdateRequest.getGlb());
        }
        if (shopUpdateRequest.getPhotos() != null && !shopUpdateRequest.getPhotos().isEmpty()) {
            shopStorageService.uploadShopPhotos(ownerId, shopId, shopUpdateRequest.getPhotos());
        }

        shopRepository.save(shop);
    }

    @Transactional(readOnly = true)
    public ShopResponse getShop(Integer shopId) {
        Shop shop = findShopById(shopId);
        String logo = shopStorageService.getPhotoUrl(shop.getLogo());
        return shopMapper.toResponse(shop, logo);
    }

    @Transactional(readOnly = true)
    public PageResponse<ShopResponse> getAllShops(Integer id, int page, int size) {
        Users user = userRepository.findById(id).orElse(null);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Shop> shopsPage;

        assert user != null;
        Optional<String> roleSeller = user.getRoles().stream().map(Role::getName).filter(name -> name.equals("ROLE_SELLER")).findFirst();
        if (roleSeller.isPresent()) {
            shopsPage = shopRepository.findByOwnerId(id, pageable);
        } else {
            shopsPage = shopRepository.findAll(pageable);
        }

        List<ShopResponse> shopResponses = shopsPage.stream()
                .map(shop -> shopMapper.toResponse(shop, shopStorageService.getPhotoUrl(shop.getLogo())))
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

    @Transactional
    public void deleteShop(Integer ownerId, Integer shopId) {
        Shop shop = findShopById(shopId);
        validateOwnership(ownerId, shop.getOwner().getId(),
                "Unauthorized: You don't own this shop to be able to delete it");

        Set<Integer> productIds = shop.getProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toSet());

        productIds.forEach(productId -> productService.deleteProduct(productId, ownerId));
        shopStorageService.deleteShopAssets(shop);
        shopRepository.delete(shop);
    }

    private Shop findShopById(Integer shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("Shop not found"));
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
}