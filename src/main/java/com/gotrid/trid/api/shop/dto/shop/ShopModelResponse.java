package com.gotrid.trid.api.shop.dto.shop;

import com.gotrid.trid.api.shop.dto.ModelDTO;

import java.util.List;

public record ShopModelResponse(
        ModelDTO model,
        List<String> images
) {
}
