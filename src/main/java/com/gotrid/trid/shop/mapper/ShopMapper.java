package com.gotrid.trid.shop.mapper;

import com.gotrid.trid.shop.domain.Shop;
import com.gotrid.trid.shop.dto.shop.ShopRequest;
import com.gotrid.trid.shop.dto.shop.ShopResponse;
import com.gotrid.trid.shop.dto.SocialDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShopMapper {
    public Shop toEntity(ShopRequest dto) {
        return Shop.builder()
                .name(dto.name())
                .category(dto.category())
                .location(dto.location())
                .description(dto.description())
                .email(dto.email())
                .phone(dto.phone())
                .build();
    }

    public ShopResponse toResponse(Shop entity) {
        List<SocialDTO> socials = entity.getSocials().stream()
                .map(social -> new SocialDTO(social.getPlatform(), social.getLink()))
                .collect(Collectors.toList());

        return ShopResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory())
                .location(entity.getLocation())
                .description(entity.getDescription())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .socials(socials)
                .build();

    }
}
