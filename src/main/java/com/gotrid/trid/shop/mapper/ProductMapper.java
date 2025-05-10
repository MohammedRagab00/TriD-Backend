package com.gotrid.trid.shop.mapper;

import com.gotrid.trid.shop.domain.product.Product;
import com.gotrid.trid.shop.dto.product.ProductRequest;
import com.gotrid.trid.shop.dto.product.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "modelAsset", ignore = true)
    @Mapping(target = "shop", ignore = true)
    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);
}