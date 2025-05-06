package com.gotrid.trid.shop.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ShopDetailResponse(
        String name,
        String category,
        String location,
        String description,
        String email,
        String phone,
        List<SocialDTO> socials
) {
}
