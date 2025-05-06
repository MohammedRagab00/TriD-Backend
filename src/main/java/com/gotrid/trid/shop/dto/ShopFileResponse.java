package com.gotrid.trid.shop.dto;

import java.time.LocalDateTime;

public record ShopFileResponse(
        Long id,
        String gltfPath,
        String binPath,
        Long userId,
        LocalDateTime createdAt
) {
}
