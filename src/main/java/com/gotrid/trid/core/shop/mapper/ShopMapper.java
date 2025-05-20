package com.gotrid.trid.core.shop.mapper;

import com.gotrid.trid.api.shop.dto.SocialDTO;
import com.gotrid.trid.api.shop.dto.ShopRequest;
import com.gotrid.trid.api.shop.dto.ShopResponse;
import com.gotrid.trid.core.shop.model.Shop;
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

    public ShopResponse toResponse(Shop entity, String logo) {
        List<SocialDTO> socials = entity.getSocials().stream()
                .map(social -> new SocialDTO(social.getPlatform(), social.getLink()))
                .collect(Collectors.toList());

        return new ShopResponse(
                entity.getId(),
                entity.getName(),
                entity.getCategory(),
                entity.getLocation(),
                entity.getDescription(),
                entity.getEmail(),
                entity.getPhone(),
                socials,
                logo
        );
    }
}
