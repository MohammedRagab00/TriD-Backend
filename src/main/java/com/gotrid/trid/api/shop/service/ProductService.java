package com.gotrid.trid.api.shop.service;

import com.gotrid.trid.common.exception.custom.product.DuplicateResourceException;
import com.gotrid.trid.common.exception.custom.product.ProductNotFoundException;
import com.gotrid.trid.common.exception.custom.shop.ShopNotFoundException;
import com.gotrid.trid.infrastructure.azure.ProductStorageService;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.core.shop.model.Shop;
import com.gotrid.trid.core.shop.model.Product;
import com.gotrid.trid.core.shop.model.ProductVariant;
import com.gotrid.trid.api.shop.dto.AssetUrlsDTO;
import com.gotrid.trid.api.shop.dto.CoordinateDTO;
import com.gotrid.trid.api.shop.dto.ModelAssetsDTO;
import com.gotrid.trid.api.shop.dto.product.ProductRequest;
import com.gotrid.trid.api.shop.dto.product.ProductResponse;
import com.gotrid.trid.api.shop.dto.product.ProductVariantRequest;
import com.gotrid.trid.api.shop.dto.product.ProductVariantResponse;
import com.gotrid.trid.core.shop.mapper.CoordinateMapper;
import com.gotrid.trid.core.shop.mapper.ProductMapper;
import com.gotrid.trid.core.shop.mapper.ProductVariantMapper;
import com.gotrid.trid.core.shop.model.Coordinates;
import com.gotrid.trid.core.shop.model.ModelAsset;
import com.gotrid.trid.core.shop.repository.ProductRepository;
import com.gotrid.trid.core.shop.repository.ProductVariantRepository;
import com.gotrid.trid.core.shop.repository.ShopRepository;
import com.gotrid.trid.infrastructure.service.BaseModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class ProductService extends BaseModelService {
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductMapper productMapper;
    private final ProductVariantMapper variantMapper;
    private final ProductStorageService productStorageService;

    @Autowired
    public ProductService(CoordinateMapper coordinateMapper,
                          ShopRepository shopRepository,
                          ProductRepository productRepository,
                          ProductVariantRepository variantRepository,
                          ProductMapper productMapper,
                          ProductVariantMapper variantMapper,
                          ProductStorageService productStorageService) {
        super(coordinateMapper);
        this.shopRepository = shopRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.productMapper = productMapper;
        this.variantMapper = variantMapper;
        this.productStorageService = productStorageService;
    }

    @Transactional
    public Integer createProduct(Integer shopId, Integer ownerId, ProductRequest request) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("Shop not found"));

        validateOwnership(ownerId, shop.getOwner().getId(),
                "Unauthorized: You don't own this shop to be able to create a product");

        Product product = productMapper.toEntity(request);
        product.setShop(shop);

        return productRepository.save(product).getId();
    }

    @Transactional
    public void updateProductCoordinates(Integer productId, Integer ownerId, CoordinateDTO coordinates) {
        Product product = findProductById(productId);
        validateOwnership(ownerId, product.getShop().getOwner().getId(),
                "Unauthorized: You don't own this shop to be able to add coordinates for this product");

        ModelAsset modelAsset = getOrCreateModelAsset(product.getModelAsset());
        Coordinates updatedCoordinates = getOrCreateCoordinates(modelAsset);

        updateCoordinates(updatedCoordinates, coordinates);
        modelAsset.setCoordinates(updatedCoordinates);
        product.setModelAsset(modelAsset);

        productRepository.save(product);
    }

    public ModelAssetsDTO getProductAssetDetails(Integer productId) {
        Product product = findProductById(productId);
        AssetUrlsDTO urls = productStorageService.getProductAssetUrls(productId);
        return createAssetDetails(product.getModelAsset(), urls);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getShopProducts(Integer shopId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Product> productsPage = productRepository.findByShopId(shopId, pageable);

        List<ProductResponse> responses = productsPage.stream()
                .map(productMapper::toResponse)
                .toList();

        return new PageResponse<>(
                responses,
                productsPage.getNumber(),
                productsPage.getSize(),
                productsPage.getTotalElements(),
                productsPage.getTotalPages(),
                productsPage.isFirst(),
                productsPage.isLast()
        );
    }

    @Transactional
    public Integer addProductVariant(Integer productId, Integer ownerId, ProductVariantRequest request) {
        Product product = findProductById(productId);
        validateOwnership(ownerId, product.getShop().getOwner().getId(),
                "Unauthorized: You don't own this shop to be able to add variants to this product");

        if (variantRepository.existsByColorAndSize(request.color(), request.size())) {
            throw new DuplicateResourceException(
                    "Product variant already exists with size: " + request.size() + " and color: " + request.color()
            );
        }

        ProductVariant variant = variantMapper.toEntity(request);
        variant.setProduct(product);

        return variantRepository.save(variant).getId();
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductVariantResponse> getProductVariants(Integer productId, int page, int size) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<ProductVariant> variantsPage = variantRepository.findByProduct_Id(productId, pageable);

        List<ProductVariantResponse> responses = variantsPage.stream()
                .map(variantMapper::toResponse)
                .toList();

        return new PageResponse<>(
                responses,
                variantsPage.getNumber(),
                variantsPage.getSize(),
                variantsPage.getTotalElements(),
                variantsPage.getTotalPages(),
                variantsPage.isFirst(),
                variantsPage.isLast()
        );
    }

    @Transactional
    public void updateProduct(Integer productId, Integer ownerId, ProductRequest request) {
        Product product = findProductById(productId);
        validateOwnership(ownerId, product.getShop().getOwner().getId(),
                "Unauthorized: You don't own this product to be able to add variants to it");

        product.setName(request.name());
        product.setSizes(request.sizes());
        product.setColors(request.colors());
        product.setDescription(request.description());
        product.setBasePrice(request.basePrice());

        productRepository.save(product);
    }

    @Transactional
    public void updateProductVariant(Integer variantId, Integer ownerId, ProductVariantRequest request) {
        ProductVariant variant = findVariantById(variantId);
        validateOwnership(ownerId, variant.getProduct().getShop().getOwner().getId(),
                "Unauthorized: You don't own this shop to be able to update this product variant");

        variant.setColor(request.color());
        variant.setSize(request.size());
        variant.setStock(request.stock());
        variant.setPrice(request.price());

        variantRepository.save(variant);
    }

    @Transactional
    public void deleteProductVariant(Integer variantId, Integer ownerId) {
        ProductVariant variant = findVariantById(variantId);
        validateOwnership(ownerId, variant.getProduct().getShop().getOwner().getId(),
                "Unauthorized: You don't own this product variant to be able to delete it");
        variantRepository.delete(variant);
    }

    @Transactional
    public void deleteProduct(Integer productId, Integer ownerId) {
        Product product = findProductById(productId);
        validateOwnership(ownerId, product.getShop().getOwner().getId(),
                "Unauthorized: You don't own this product to be able to delete it");

        productStorageService.deleteProductAssets(productId);
        productRepository.delete(product);
    }

    @Transactional
    public void uploadProductAssets(Integer productId, Integer ownerId, MultipartFile gltfFile,
                                    MultipartFile binFile, MultipartFile iconFile, MultipartFile textureFile) {
        productStorageService.uploadProductAssets(productId, ownerId, gltfFile, binFile, iconFile, textureFile);
    }

    private Product findProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with id: " + productId
                ));
    }

    private ProductVariant findVariantById(Integer variantId) {
        return variantRepository.findById(variantId)
                .orElseThrow(() -> new ProductNotFoundException("Product variant not found with id: " + variantId));
    }

    public ProductResponse getProduct(Integer productId) {
        Product product = findProductById(productId);
        return productMapper.toResponse(product);
    }
}