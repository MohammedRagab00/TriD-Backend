package com.gotrid.trid.api.shop.dto.shop;

import com.gotrid.trid.api.shop.dto.SocialDTO;

import java.util.List;

public record ShopResponse(
        Integer id,
        String name,
        String category,
        String location,
        String description,
        String email,
        String phone,
        List<SocialDTO> socials,
        String logo
) {
}
