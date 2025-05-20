package com.gotrid.trid.api.shop.dto;

import com.gotrid.trid.core.threedModel.dto.ModelDTO;

import java.util.List;

public record ShopModelResponse(
        ModelDTO model,
        List<String> images
) {
}
