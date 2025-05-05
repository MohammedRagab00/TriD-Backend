package com.gotrid.trid.marshmello.dto.Users;

import java.time.LocalDateTime;

public record ShopFileResponse(
        Long id,
        String gltfPath,
        String binPath,
        Long userId,
        LocalDateTime createdAt
) {}
