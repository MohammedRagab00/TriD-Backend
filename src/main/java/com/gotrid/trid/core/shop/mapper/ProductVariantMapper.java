package com.gotrid.trid.core.shop.mapper;

import com.gotrid.trid.core.shop.model.ProductVariant;
import com.gotrid.trid.api.shop.dto.product.ProductVariantRequest;
import com.gotrid.trid.api.shop.dto.product.ProductVariantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    @Mapping(target = "product", ignore = true)
    ProductVariant toEntity(ProductVariantRequest request);

    ProductVariantResponse toResponse(ProductVariant variant);

    List<ProductVariantResponse> toResponseList(List<ProductVariant> variants);
}
