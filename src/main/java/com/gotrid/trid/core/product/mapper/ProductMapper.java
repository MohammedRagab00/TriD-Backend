package com.gotrid.trid.core.product.mapper;

import com.gotrid.trid.api.product.dto.ProductRequest;
import com.gotrid.trid.api.product.dto.ProductResponse;
import com.gotrid.trid.core.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "model", ignore = true)
    @Mapping(target = "shop", ignore = true)
    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);
}